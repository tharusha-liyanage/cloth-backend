package com.project2.secondProject.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dkbpbbb8k",
                "api_key", "346823649782849",
                "api_secret", "RiWlq-qgxKEmT4ee15221RdkhbA"
        ));
    }
}
