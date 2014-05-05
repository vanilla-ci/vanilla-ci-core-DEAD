package com.vanillaci.core.controller;

import com.vanillaci.core.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jms.core.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import javax.jms.*;

/**
 * @author Joel Johnson
 */
@Controller
public class TestController {
	@Autowired
	private JmsTemplate jmsTemplate;

	@RequestMapping("/test")
	public View viewTest(
		@RequestParam(value="name", required = false, defaultValue = "Joel") String name,
		@RequestParam(value="count") int count
	) throws JMSException {
		TestObject testObject = new TestObject();
		testObject.setName(name);

		for(int i = 0; i < count; i++) {
			int currentCount = i;
			jmsTemplate.send(session -> getMessage(session, testObject.getName(), currentCount));
		}

		return new JsonView<>(testObject);
	}

	private static Message getMessage(Session session, String value, int count) throws JMSException {
		TextMessage message = session.createTextMessage(value);
		message.setIntProperty("messageCount", count);
		return message;
	}
}

class TestObject {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

