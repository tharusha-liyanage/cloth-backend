package com.project2.secondProject.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class OfferSectionDTO {
    private Long id;
    private String title1;
    private String title2;
    private MultipartFile image;
}
