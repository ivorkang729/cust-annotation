package com.example.demo.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.anno.MyAuthRequired;

@RestController
public class MyAPIController {
	
	private Map<String, List<String>> roleAuthSettings;
	
	@PostConstruct
	private void postConstruct() {
		roleAuthSettings = new HashMap<>();
		roleAuthSettings.put("ROLD_ADMIN", Arrays.asList("query", "delete"));
		roleAuthSettings.put("ROLD_USER", Arrays.asList("query"));
	}
	

	@GetMapping("/initPage")
	public String initPage(@RequestParam() String role) {
		return "Do Init Page, User has role: " + role;
	}
	
	@MyAuthRequired(authId = "query")
	@GetMapping("/query")
	public String query(@RequestParam() String role) {
		return "Do Query, User has role: " + role;
	}
	
	@MyAuthRequired(authId = "delete")
	@GetMapping("/delete")
	public String delete(@RequestParam String role) {
		return "Do Delete, User has role: " + role;
	}
	
	

}
