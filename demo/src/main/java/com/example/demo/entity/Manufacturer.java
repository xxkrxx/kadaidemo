package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // メーカー名フィールド。空白を許可せず、最大255文字までの制限
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // エンティティが新しく挿入される前に呼ばれるメソッド。
    // このメソッドで、作成日時と更新日時を現在の日時に設定
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // エンティティが更新される前に呼ばれるメソッド。
    // このメソッドで、更新日時を現在の日時に設定
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}


