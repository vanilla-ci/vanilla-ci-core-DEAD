package com.vanillaci.core.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * @author Joel Johnson
 */
public class SimpleMessageListener implements MessageListener {
	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			String text = msg.getText();
			int messageCount = msg.getIntProperty("messageCount");
			System.out.println("processing message #" + messageCount + ": " + text);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}
