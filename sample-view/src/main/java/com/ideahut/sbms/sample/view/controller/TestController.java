package com.ideahut.sbms.sample.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {

	@GetMapping({"/", "/test"})
    public String test(Model model, @RequestParam(value="name", required=false, defaultValue="Test") String name) {
        model.addAttribute("name", name);
        return "test";
    }
	
}
