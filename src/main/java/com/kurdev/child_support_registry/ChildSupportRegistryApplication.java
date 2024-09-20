package com.kurdev.child_support_registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ChildSupportRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChildSupportRegistryApplication.class, args);
	}

}
