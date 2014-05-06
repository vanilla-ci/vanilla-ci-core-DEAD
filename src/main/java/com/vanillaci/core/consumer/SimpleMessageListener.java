package com.vanillaci.core.consumer;

import com.vanillaci.core.model.*;

import javax.jms.*;


/**
 * @author Joel Johnson
 */
public class SimpleMessageListener implements MessageListener {
	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage msg = (ObjectMessage) message;
			TestObject object = (TestObject) msg.getObject();

			System.out.println("Processed " + object);

		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}
