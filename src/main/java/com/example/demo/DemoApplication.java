package com.example.demo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.yuequan.jpa.soft.delete.repository.EnableJpaSoftDeleteRepositories;

@SpringBootApplication
@ComponentScan({
		"com.example",
		"com.example",
		"com.example.demo",
		"com.example.demo.api"
})
@EnableJpaSoftDeleteRepositories
@EnableJpaAuditing
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
