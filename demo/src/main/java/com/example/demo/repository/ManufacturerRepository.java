package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Manufacturer;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}
