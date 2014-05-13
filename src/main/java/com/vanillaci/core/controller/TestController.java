package com.vanillaci.core.controller;

import com.vanillaci.core.model.*;
import com.vanillaci.core.spring.views.*;
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
	private JmsTemplate producerTemplate;

	@RequestMapping("/test")
	public View viewTest(
		@RequestParam(value="name") String name
	) throws JMSException {
		TestObject testObject = new TestObject();
		testObject.setName(name);

		producerTemplate.send(session -> session.createObjectMessage(testObject));

		return new JsonView<>(testObject);
	}
}

