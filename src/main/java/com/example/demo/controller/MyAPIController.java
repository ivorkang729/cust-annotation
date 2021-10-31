package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.anno.MyActionLog;
import com.example.demo.anno.MyAuthRequired;

@RestController
public class MyAPIController {
	
	
	
	@MyActionLog(action = "ACT_INITPAGE")
	@GetMapping("/initPage")
	public String initPage(@RequestParam() String role) {
		return "Do Init Page, User has role: " + role;
	}
	
	@MyActionLog(action = "ACT_QUERY")
	@MyAuthRequired(authId = "query")
	@GetMapping("/query")
	public String query(@RequestParam() String role) {
		return "Do Query, User has role: " + role;
	}
	
	@MyActionLog(action = "ACT_DELETE")
	@MyAuthRequired(authId = "delete")
	@GetMapping("/delete")
	public String delete(@RequestParam String role) {
		return "Do Delete, User has role: " + role;
	}
	
	

}
