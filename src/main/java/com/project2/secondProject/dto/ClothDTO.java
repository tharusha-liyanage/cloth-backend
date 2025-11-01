package com.project2.secondProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClothDTO {
    private String clothName;
    private double price;
    private List<String> sizes;
    private String clothType;
    private Map<String, Integer> stockCount;
    private MultipartFile image; // ✅ FIXED
}
