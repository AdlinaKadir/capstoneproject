package com.collabera.capstoneeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CapstoneeurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneeurekaApplication.class, args);
	}

}
