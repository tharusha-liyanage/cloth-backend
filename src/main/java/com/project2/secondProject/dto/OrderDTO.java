package com.project2.secondProject.dto;


import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
    private double totalAmount;
    private String status;
    private List<OrderItemDTO> items;
}

