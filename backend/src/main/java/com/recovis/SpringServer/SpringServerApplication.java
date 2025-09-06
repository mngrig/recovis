package com.recovis.SpringServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.recovis.SpringServer")
public class SpringServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringServerApplication.class, args);



	}

}
