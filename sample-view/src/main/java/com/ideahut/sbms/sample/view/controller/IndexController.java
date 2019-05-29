package com.ideahut.sbms.sample.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	/*
	 * @GetMapping({"/", "/test"}) public String test(Model
	 * model, @RequestParam(value="name", required=false, defaultValue="Test")
	 * String name) { model.addAttribute("name", name); return "test"; }
	 */
	
	@GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
	
}
