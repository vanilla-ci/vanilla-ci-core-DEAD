package com.vanillaci.core.controller;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

/**
 * @author Joel Johnson
 */
@Controller
public class HelloWorldController {
	@RequestMapping("/hello")
	public String hello(
		@RequestParam(value = "name", required = false, defaultValue = "World") String name,
		Model model
	) {
		model.addAttribute("name", name);
		return "helloworld";
	}
}
