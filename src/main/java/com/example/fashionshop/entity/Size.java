package com.example.fashionshop.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "sizes")
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "size")
    private List<ProductVariant> variants;
}