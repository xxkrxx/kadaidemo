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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Administrator {

    // 主キーとなるIDフィールド
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // メールアドレスフィールド。空白不可、メール形式、255文字以内
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(min = 5, max = 255)
    private String email;

    // パスワードフィールド。空白不可、8文字以上64文字以内
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;

    // 姓フィールド。空白不可、1文字以上50文字以内
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50)
    private String lastName;

    // 名フィールド。空白不可、1文字以上50文字以内
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50)
    private String firstName;

    // 電話番号フィールド。空白不可、数値のみ、10文字以上15文字以内
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must be numeric")
    @Size(min = 10, max = 15)
    private String phone;

    // Storeエンティティとの多対一リレーション。null不可
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull(message = "Store is required")
    @ToString.Exclude // ToStringでこのフィールドを除外
    private Store store;

    // Roleエンティティとの多対多リレーション。fetch = FetchType.EAGERで即時ロード。null不可
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "administrator_roles",
      joinColumns = @JoinColumn(name = "administrator_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
    @NotNull(message = "Roles are required")
    @ToString.Exclude // ToStringでこのフィールドを除外
    private Set<Role> roles;

    // Postエンティティとの多対多リレーション。fetch = FetchType.EAGERで即時ロード。null不可
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "administrator_posts",
      joinColumns = @JoinColumn(name = "administrator_id"),
      inverseJoinColumns = @JoinColumn(name = "post_id"))
    @NotNull(message = "Posts are required")
    @ToString.Exclude // ToStringでこのフィールドを除外
    private Set<Post> posts;
}

