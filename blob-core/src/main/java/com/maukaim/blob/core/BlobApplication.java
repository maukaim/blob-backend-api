package com.maukaim.blob.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BlobApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlobApplication.class, args);
	}

}
