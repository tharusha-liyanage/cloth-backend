package com.project2.secondProject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecondProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecondProjectApplication.class, args);
	}
@Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
