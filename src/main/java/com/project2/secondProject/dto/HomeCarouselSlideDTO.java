package com.project2.secondProject.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HomeCarouselSlideDTO {
    private String title;
    private String subtitle;
    private MultipartFile image;
}
