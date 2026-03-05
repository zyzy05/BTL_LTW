package com.example.fashionshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String addressLine;

    private String city;

    private String district;

    private String ward;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}