package com.project2.secondProject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // link back to order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // snapshot of cloth at purchase
    private Long clothId;
    private String clothName;
    private String imageUrl;
    private double price;

    // purchased size and quantity
    private String size;
    private int quantity;

    private double subTotal;
}

