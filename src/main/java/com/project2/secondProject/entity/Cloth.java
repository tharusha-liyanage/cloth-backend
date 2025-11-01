package com.project2.secondProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Map;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Cloth")
public class Cloth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clothName;

    private double price;

    @ElementCollection
    @CollectionTable(name = "cloth_sizes", joinColumns = @JoinColumn(name = "cloth_id"))
    @Column(name = "size")
    private List<String> sizes; // XS, S, M, L, XL, XXL

    private String clothType; // e.g. Denim, Office Wear, etc.

    @ElementCollection
    @CollectionTable(name = "cloth_stock", joinColumns = @JoinColumn(name = "cloth_id"))
    @MapKeyColumn(name = "size")
    @Column(name = "count")
    private Map<String, Integer> stockCount; // size → stock count

    private String imageUrl;

}
