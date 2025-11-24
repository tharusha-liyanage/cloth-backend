package com.project2.secondProject.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long clothId;
    private String size;
    private int quantity;
}
