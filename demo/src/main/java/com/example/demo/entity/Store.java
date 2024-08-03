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
    // 店舗名。空白であってはならない（バリデーション）
    private String name;

    @NotBlank(message = "住所は必須です")
    // 住所。空白であってはならない（バリデーション）
    private String address;

    // レコードの作成日時
    private LocalDateTime createdAt;

    // レコードの更新日時
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "store")
    @ToString.Exclude
    // 店舗に関連する製品のリスト。双方向の関連を避けるためにToStringから除外
    private List<StoreProduct> storeProducts;

    @PrePersist
    protected void onCreate() {
        // エンティティが初めて保存される前に実行されるメソッド
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        // エンティティが更新される前に実行されるメソッド
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        // エンティティの文字列表現をカスタマイズ
        return "Store{id=" + id + 
                ", name='" + name + '\'' + 
                ", address='" + address + '\'' + 
                ", createdAt=" + createdAt + 
                ", updatedAt=" + updatedAt + '}';
    }
}

