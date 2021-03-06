package com.developairs.service;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.developairs.data.GCDRepository;
import com.developairs.data.MessageRepository;
import com.developairs.exception.GCDAppException;
import com.developairs.exception.ResponseCode;
import com.developairs.model.GCD;
import com.developairs.model.Message;

/**
 * 
 * @author Ali Abazari
 * <p>This class is an EJB stateless session bean which handles all requests come from clients either SOAP client or REST clients</p>
 *
 *
 */
@Stateless
public class MessageHandler {

	@Inject
	private Logger log;

	@Inject
	private MessageRepository messageRepository;

	/*@Inject
	private Event<Message> memberEventSrc;*/

	@Inject
	private GCDRepository gcdRepository;

	@Inject
	@JMSConnectionFactory("java:/ConnectionFactory")
	private JMSContext context;

	@Resource(name="java:/jms/CGDQueue")
	private Destination destination;

	@Resource
	private ConnectionFactory connectionFactory;

	/**
	 * 
	 * <p>This method is just allowed for the users with the role <b>restclient</b><p>
	 * @param i1 is the first integer which will be added to the database and the JMS queue
	 * @param i2 is the second integer which will be added to the database and the JMS queue
	 * @throws GCDAppException if something goes wrong, application will throw custom exception
	 * @throws javax.ejb.EJBAccessExceptio if the caller user does not have the required role
	 */
	@RolesAllowed("restclient")
	public void handleMessages(int i1, int i2) throws GCDAppException {
		log.fine("Registering <" + i1 + "," + i2 + ">");
		Message m1 = new Message(i1);
		Message m2 = new Message(i2);

		this.saveMessage(m1);
		this.saveMessage(m2);

		JMSProducer producer = context.createProducer();

		ObjectMessage om1 = null, om2 = null;
		try {
			om1 = context.createObjectMessage();
			om2 = context.createObjectMessage();
			om1.setObject(m1.getNumber());
			om2.setObject(m2.getNumber());
		} catch (Exception e) {
			log.severe("Unable to create ObjectMessage: " + e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_OBJECT_MSG, "Unable to create ObjectMessage", e);
		}

		try {
			producer.send(destination, om1);
			producer.send(destination, om2);
		} catch (Exception e) {
			log.severe("Unable to send on queue: " + e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_SEND_QUEUE, "Unable to send on queue", e);
		}
		//		memberEventSrc.fire(m1);
		//		memberEventSrc.fire(m2);
	}

	private void saveMessage(Message message){
		try {
			messageRepository.save(message);
		} catch (Exception e) {
			log.severe("Unable to save Message to db: "+e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_DB, "Unable to save Message to db", e);
		}
	}

	/**
	 * <p>This method is just allowed for the users with the role <b>restclient</b><p>
	 * @return list of all messages stored in db
	 * @throws GCDAppException if something goes wrong, application will throw custom exception
	 * @throws javax.ejb.EJBAccessExceptio if the caller user does not have the required role
	 */
	@RolesAllowed("restclient")
	public List<Integer> getAllMessages() throws GCDAppException{
		try {
			return messageRepository.findAllNumbersOrderByDate();
		} catch (Exception e) {
			log.severe("Unable to get all messages from db: "+e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_DB, "Unable to get all messages from db", e);
		}
	}

	/**
	 * <p>This method is just allowed for the users with the role <b>soapclient</b><p>
	 * @return the calculated GCD from the first two integers in the queue
	 * @throws GCDAppException if something goes wrong, application will throw custom exception
	 * @throws javax.ejb.EJBAccessExceptio if the caller user does not have the required role
	 */
	@RolesAllowed("soapclient")
	public int getGCD() throws GCDAppException {

		javax.jms.Message message = null;
		javax.jms.Message message2 = null;
		try {
			message = this.readAMessageFromQueue();
			message2 = this.readAMessageFromQueue();
		} catch (Exception e) {
			log.warning("Unable to read from the queue: "+e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_RECEIVE_QUEUE, "Unable to read from the queue", e);
		}

		int i1 = -1;
		int i2 = -1;
		try {
			ObjectMessage om = (ObjectMessage)message;
			i1 = (Integer)om.getObject();
			om = (ObjectMessage)message2;
			i2 = (Integer)om.getObject();
		} catch (Exception e) {
			log.severe("Unable to cast data which is read from the queue: "+e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_RECEIVE_QUEUE, "Unable to cast data which is read from the queue", e);
		}

		int calculatedGCD = calcGCD(i1, i2);
		GCD gcd = new GCD(calculatedGCD);

		this.saveGCD(gcd);
		return calculatedGCD;
	}

	private javax.jms.Message readAMessageFromQueue() throws JMSException{
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession();

		MessageConsumer messageConsumer = session.createConsumer(destination);
		connection.start();
		javax.jms.Message message = null;
		try {
			message = messageConsumer.receive(2000);
			if (message == null) {
				throw new JMSException("Timeout");
			}
		} catch (JMSException e) {
			connection.close();
			throw e;
		}

		connection.close();
		return message;
	}

	private int calcGCD(int a, int b) throws GCDAppException {
		try {
			BigInteger b1 = BigInteger.valueOf(a);
			BigInteger b2 = BigInteger.valueOf(b);
			BigInteger gcd = b1.gcd(b2);
			return gcd.intValue();
		} catch (Exception e) {
			log.severe("Unable to calculate GCD: "+e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_CALCULATE, "Unable to calculate GCD", e);
		}
	}

	/**
	 * <p>This method is just allowed for the users with the role <b>soapclient</b><p>
	 * @return the list of calculated GCDs from database
	 * @throws GCDAppException if something goes wrong, application will throw custom exception
	 * @throws javax.ejb.EJBAccessExceptio if the caller user does not have the required role
	 */
	@RolesAllowed("soapclient")
	public List<Integer> getGcdList() throws GCDAppException{
		try {
			return gcdRepository.getAllGCDOrderbyDate();
		} catch (Exception e) {
			log.severe("Unable to get all GCDs from db: "+e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_DB, "Unable to get all GCDs from db", e);
		}
	}

	/**
	 * <p>This method is just allowed for the users with the role <b>soapclient</b><p>
	 * @return the sum of the calculated GCDs
	 * @throws GCDAppException if something goes wrong, application will throw custom exception
	 * @throws javax.ejb.EJBAccessExceptio if the caller user does not have the required role
	 */
	@RolesAllowed("soapclient")
	public long getGcdSum() throws GCDAppException{
		try {
			List<Integer> allGCD = gcdRepository.getSumOfAllGCD();
			if (allGCD == null || allGCD.isEmpty()) {
				return -1;
			}
			return allGCD.stream().mapToInt(Integer::intValue).sum();
		} catch (Exception e) {
			log.severe("Unable to get sum of all GCDs from db: "+e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_DB, "Unable to get sum of all GCDs from db", e);
		}
	}

	private void saveGCD(GCD gcd) throws GCDAppException{
		try {
			gcdRepository.save(gcd);
		} catch (Exception e) {
			log.severe("Unable to save GCD to the db: "+e.getMessage());
			throw new GCDAppException(ResponseCode.ERR_DB, "Unable to save GCD to the db", e);
		}
	}
}