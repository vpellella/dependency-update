package com.dependency.update;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dependency.update.service.DependencyUpdateService;

@SpringBootApplication
public class DependencyUpdateApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(DependencyUpdateApplication.class, args);
		
	}

	@Bean
	public CommandLineRunner runOnStartup(DependencyUpdateService updateService) {
	    return args -> updateService.updateDependency();
	}
}
