package com.project2.secondProject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One item belongs to one cart
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // Which product
    @ManyToOne
    @JoinColumn(name = "cloth_id")
    private Cloth cloth;

    // Size selected (S, M, L..)
    private String size;

    // Quantity
    private int quantity;
}
