package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class SmallCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "middle_category_id")
    private MiddleCategory middleCategory;

    @Override
    public String toString() {
        return "SmallCategory{id=" + id + ", name='" + name + "', middleCategory=" + middleCategory.getName() + "}";
    }
}