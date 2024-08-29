package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Manufacturer;

public interface ManufacturerService {
    // 全てのメーカー情報を取得する
    List<Manufacturer> getAllManufacturers();

    // IDでメーカー情報を取得する
    Optional<Manufacturer> getManufacturerById(Long id);

    // メーカー情報を保存する
    Manufacturer saveManufacturer(Manufacturer manufacturer);

    // IDでメーカー情報を削除する
    void deleteManufacturerById(Long id);
}
