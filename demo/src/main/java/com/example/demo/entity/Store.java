package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "stores")
@Data
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "店舗名は必須です")
    private String name;

    @NotBlank(message = "住所は必須です")
    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "store")
    @ToString.Exclude
    private List<StoreProduct> storeProducts;

    @OneToMany(mappedBy = "store")
    @ToString.Exclude
    private List<Administrator> administrators; 

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Store{id=" + id + 
                ", name='" + name + '\'' + 
                ", address='" + address + '\'' + 
                ", createdAt=" + createdAt + 
                ", updatedAt=" + updatedAt + '}';
    }

    // 新しく追加されたメソッド
    public Administrator getAdmin() {
        // ここでは最初の管理者を返す例として実装
        return administrators.isEmpty() ? null : administrators.get(0);
    }
}


