package com.developairs.service;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
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
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.developairs.model.GCD;
import com.developairs.model.Message;

@Stateless
public class MessageHandler {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Message> memberEventSrc;

	public void register(int i1, int i2) throws Exception{
		log.info("Registering <" + i1 + "," + i2 + ">");
		Message m1 = new Message(i1);
		Message m2 = new Message(i2);

		em.persist(m1);
		em.persist(m2);

		JMSProducer producer = context.createProducer();

		ObjectMessage om1 = context.createObjectMessage();
		om1.setObject(m1.getNumber());

		ObjectMessage om2 = context.createObjectMessage();
		om2.setObject(m2.getNumber());

		producer.send(destination, om1);
		producer.send(destination, om2);

		memberEventSrc.fire(m1);
		memberEventSrc.fire(m2);
	}

	@Inject
	@JMSConnectionFactory("java:/ConnectionFactory")
	private JMSContext context;

	@Resource(name="java:/jms/CGDQueue")
	private Destination destination;

	@SuppressWarnings("unchecked")
	public List<Integer> getAllMessages() {
		Query createQuery = em.createQuery("SELECT m.number from Message m ORDER BY m.addedDate");
		List<Integer> resultList = createQuery.getResultList();
		return resultList;
	}

	@Resource
	private ConnectionFactory connectionFactory;

	public int getGCD() throws Exception{
		//TODO refactor
		
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		MessageConsumer messageConsumer = session.createConsumer(destination);
		connection.start();
		javax.jms.Message message = null;
		try {
			message = messageConsumer.receive(2000);
			if (message == null) {
				throw new JMSException("Timeout");
			}
		} catch (JMSException e) {
			// TODO: handle exception
			connection.close();
			throw e;
		}
		int i1 = -1;
		try {
			ObjectMessage om = (ObjectMessage)message;
			i1 = (Integer)om.getObject();
			System.out.println(i1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			connection.close();
			throw e;
		}

		try {
			message = messageConsumer.receive(2000);
			if (message == null) {
				throw new JMSException("Timeout");
			}
		} catch (JMSException e) {
			// TODO: handle exception
			connection.close();
			throw e;
		}
		
		int i2 = -1;
		try {
			ObjectMessage om = (ObjectMessage)message;
			i2 = (Integer)om.getObject();
			System.out.println(i2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}finally{
			connection.close();
		}


		int calculatedGCD = calcGCD(i1, i2);
		GCD gcd = new GCD(calculatedGCD);
		em.persist(gcd);
		return calculatedGCD;
	}

	private int calcGCD(int a, int b) {
		BigInteger b1 = BigInteger.valueOf(a);
		BigInteger b2 = BigInteger.valueOf(b);
		BigInteger gcd = b1.gcd(b2);
		return gcd.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Integer> gcdList(){
		Query createQuery = em.createQuery("SELECT g.value from GCD g ORDER BY g.addedDate");
		List<Integer> resultList = createQuery.getResultList();
		return resultList;
	}

	public long gcdSum(){
		Query createQuery = em.createQuery("SELECT SUM(g.value) from GCD g");
		return (long) createQuery.getSingleResult();
	}
}