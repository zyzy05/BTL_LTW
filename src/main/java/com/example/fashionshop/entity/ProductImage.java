package com.example.fashionshop.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imageUrl;

    private Boolean isMain;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}