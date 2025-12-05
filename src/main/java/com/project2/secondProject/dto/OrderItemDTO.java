package com.project2.secondProject.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long clothId;
    private String clothName;
    private String imageUrl;
    private double price;
    private String size;
    private int quantity;
    private double subTotal;
}
