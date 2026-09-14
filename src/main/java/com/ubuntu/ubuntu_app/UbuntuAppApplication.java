package com.ubuntu.ubuntu_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan("com.ubuntu.ubuntu_app.shared.config")
public class UbuntuAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(UbuntuAppApplication.class, args);
	}


}
