package com.test.kamal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class KamalDemoApplication {
	@GetMapping(path = "/hello")
	String get() {
		return "GET Response";
	}

	public static void main(String[] args) {
		SpringApplication.run(KamalDemoApplication.class, args);
	}

}
