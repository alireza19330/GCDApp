package com.developairs.test;

import java.lang.reflect.Member;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.developairs.data.MessageRepository;
import com.developairs.exception.GCDAppException;
import com.developairs.model.Message;
import com.developairs.service.MessageHandler;
import com.developairs.util.Resources;
import com.developairs.util.test.RestUser;
import com.developairs.util.test.SoapUser;

@RunWith(Arquillian.class)
public class JMSQueueTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackage(MessageRepository.class.getPackage())
				.addPackage(MessageHandler.class.getPackage())
				.addPackage(Message.class.getPackage())
				.addPackage(GCDAppException.class.getPackage())
				.addPackage(SoapUser.class.getPackage())
				.addClasses(Member.class, Message.class, Resources.class, MessageHandler.class, MessageRepository.class)
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test datasource
				.addAsWebInfResource("test-ds.xml", "test-ds.xml");
	}

	@Inject
	private Logger log;

	@EJB
	private MessageHandler messageHandler;

	@Inject
	private RestUser restuser;

	private int i1 = 10;
	private int i2 = 20;

	@Test
	@InSequence(1)
	public void testRestServices() throws Exception {
		restuser.call(new Callable() {

			@Override
			public Object call() throws Exception {
				messageHandler.handleMessages(i1, i2);
				List<Integer> allMessages = messageHandler.getAllMessages();
				Assert.assertEquals(i1,(int)allMessages.get(0));
				Assert.assertEquals(i2,(int)allMessages.get(1));
				log.info(" >>>>>>>>>>>> messages added to the queue and db.");
				return null;
			}
		});
	}

	@Inject
	private SoapUser soapUser;

	@Test
	@InSequence(2)
	public void getGCD() throws Exception{
		soapUser.call(new Callable() {

			@Override
			public Object call() throws Exception {
				int gcd = messageHandler.getGCD();
				log.info(">>>>>>>>GCD: "+gcd);
				Assert.assertEquals(calcGCD(i1, i2), gcd);
				return null;
			}
		});
	}

	@Test
	@InSequence(3)
	public void getGCDAgain() throws Exception{
		soapUser.call(new Callable() {

			@Override
			public Object call() throws Exception {
				int nextGCD = -1;
				try {
					nextGCD = messageHandler.getGCD();
					log.info(">>>>>>>>nextGCD: "+nextGCD);
				} catch (GCDAppException e) {
					log.severe(">>>>>>>> error:"+e.getMessage());
				}
				Assert.assertEquals(-1, nextGCD);
				return null;
			}});
	}

	@Test
	@InSequence(4)
	public void getAllCalculatedGCDs() throws Exception{
		soapUser.call(new Callable() {

			@Override
			public Object call() throws Exception {
				List<Integer> gcdList = messageHandler.getGcdList();
				log.info("Size of the list:"+(gcdList == null ? "-" : gcdList.size()));
				int gcd = gcdList.get(0);
				Assert.assertEquals(calcGCD(i1, i2), gcd);
				return null;
			}
		});
	}

	private int calcGCD(int a, int b) {
		BigInteger b1 = BigInteger.valueOf(a);
		BigInteger b2 = BigInteger.valueOf(b);
		BigInteger gcd = b1.gcd(b2);
		return gcd.intValue();
	}

	//TODO add more test

}
