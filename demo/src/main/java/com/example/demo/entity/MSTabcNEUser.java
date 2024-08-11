package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "mstabcneuser") 
@Data
public class MSTabcNEUser {

    @Id
    private Long id; 

    private String property1;
    private String property2;
    
}
