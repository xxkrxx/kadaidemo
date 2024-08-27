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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(min = 5, max = 255)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50)
    private String lastName;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must be numeric")
    @Size(min = 10, max = 15)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @NotNull(message = "Store is required")
    @ToString.Exclude
    private Store store;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "administrator_roles",
      joinColumns = @JoinColumn(name = "administrator_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
    @NotNull(message = "Roles are required")
    @ToString.Exclude
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "administrator_posts",
      joinColumns = @JoinColumn(name = "administrator_id"),
      inverseJoinColumns = @JoinColumn(name = "post_id"))
    @NotNull(message = "Posts are required")
    @ToString.Exclude
    private Set<Post> posts;

    // フルネームを返すメソッド
    public String name() {
        return this.firstName + " " + this.lastName;
    }
}