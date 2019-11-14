package com.want2play.want2play;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WantToPlayCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(WantToPlayCoreApplication.class, args);
	}

}
