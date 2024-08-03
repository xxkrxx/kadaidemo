package com.example.demo.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // EqualsとHashCodeをIDのみで比較
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自動生成されるID
    @EqualsAndHashCode.Include  // EqualsとHashCodeの比較にIDを含む
    private Long id;

    @NotBlank(message = "Email is required")  // 空でないことを保証
    @Email(message = "Invalid email format")  // メール形式であることを保証
    @Size(min = 5, max = 255)  // メールアドレスの長さを制限
    private String email;

    @NotBlank(message = "Password is required")  // 空でないことを保証
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")  // パスワードの長さを制限
    private String password;

    @NotBlank(message = "Last name is required")  // 空でないことを保証
    @Size(min = 1, max = 50)  // 苗字の長さを制限
    private String lastName;

    @NotBlank(message = "First name is required")  // 空でないことを保証
    @Size(min = 1, max = 50)  // 名前の長さを制限
    private String firstName;

    @NotBlank(message = "Phone number is required")  // 空でないことを保証
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must be numeric")  // 数字のみであることを保証
    @Size(min = 10, max = 15)  // 電話番号の長さを制限
    private String phone;

    @ManyToOne  // 多対一の関係（UserとStore）
    @JoinColumn(name = "store_id", nullable = false)  // Storeエンティティとの関連を定義する外部キー
    @NotNull(message = "Store is required")  // 空でないことを保証
    @ToString.Exclude  // toString()メソッドから除外
    private Store store;

    @ManyToMany(fetch = FetchType.EAGER)  // 多対多の関係（UserとRole）
    @JoinTable(
      name = "user_roles",  // 中間テーブルの名前
      joinColumns = @JoinColumn(name = "user_id"),  // Userエンティティの外部キー
      inverseJoinColumns = @JoinColumn(name = "role_id"))  // Roleエンティティの外部キー
    @NotNull(message = "Roles are required")  // 空でないことを保証
    @ToString.Exclude  // toString()メソッドから除外
    private Set<Role> roles;  // Userが持つ役割の集合

    @ManyToMany(fetch = FetchType.EAGER)  // 多対多の関係（UserとPost）
    @JoinTable(
      name = "user_posts",  // 中間テーブルの名前
      joinColumns = @JoinColumn(name = "user_id"),  // Userエンティティの外部キー
      inverseJoinColumns = @JoinColumn(name = "post_id"))  // Postエンティティの外部キー
    @NotNull(message = "Posts are required")  // 空でないことを保証
    @ToString.Exclude  // toString()メソッドから除外
    private Set<Post> posts;  // Userが持つポストの集合

    // 管理者フラグ
    private boolean admin;

    // isAdmin メソッド
    public boolean isAdmin() {
        return admin;
    }
}
