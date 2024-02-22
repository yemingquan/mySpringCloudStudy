package com.example.springBootDemo;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@SpringBootApplication
@EnableKnife4j
@EnableScheduling//开启定时任务
@EnableTransactionManagement
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
