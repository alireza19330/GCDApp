package com.developairs.test;

import java.lang.reflect.Member;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.developairs.data.MessageRepository;
import com.developairs.model.Message;
import com.developairs.service.MessageHandler;
import com.developairs.util.Resources;

@RunWith(Arquillian.class)
public class JMSQueueTest {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackage(MessageRepository.class.getPackage())
				.addPackage(MessageHandler.class.getPackage())
				.addPackage(Message.class.getPackage())
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

	@Test
	public void testSaveMessage() throws Exception {
		int i1 = 10;
		int i2 = 20;
		messageHandler.handleMessages(i1, i2);
		Assert.assertEquals(i1,(int)messageHandler.getAllMessages().get(0));
		Assert.assertEquals(i2,(int)messageHandler.getAllMessages().get(1));
		log.info("messages can be added");
	}
	
	//TODO add more test

}
