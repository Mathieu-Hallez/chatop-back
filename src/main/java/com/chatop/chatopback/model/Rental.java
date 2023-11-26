package com.chatop.chatopback.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="RENTALS")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal surface;

    private BigDecimal price;

    private String picture;

    private String description;

    @ManyToOne
    private DBUser user;

    @Column(name="created_at")
    private Timestamp createdAt;

    @Column(name="updated_at")
    private Timestamp updatedAt;
}
