package com.example.springBootDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@EnableEurekaClient
@SpringBootApplication
public class SpringBootDemoApplication {

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World";
	}

	public static void main(String[] args) {
		System.out.println("hello world");
		SpringApplication.run(SpringBootDemoApplication.class, args);
        System.out.println("success!");
	}

}
