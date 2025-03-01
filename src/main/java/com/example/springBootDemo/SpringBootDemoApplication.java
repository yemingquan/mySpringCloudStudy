package com.example.springBootDemo;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
        System.out.println("success!");
	}

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World";
	}
}
