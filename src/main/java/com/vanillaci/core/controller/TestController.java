package com.vanillaci.core.controller;

import com.vanillaci.core.json.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

/**
 * @author Joel Johnson
 */
@Controller
public class TestController {
	@RequestMapping("/test")
	public View viewTest(@RequestParam(value="name", required = false, defaultValue = "Joel") String name) {
		TestObject testObject = new TestObject();
		testObject.setName(name);

		return new JsonView<>(testObject);
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

