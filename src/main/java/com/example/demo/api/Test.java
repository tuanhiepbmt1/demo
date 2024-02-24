package com.example.demo.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Test {
	@GetMapping("/test")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String test() {
		return "testing";
	}
}
