package com.example.Elfagr.User.Entity;


import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import com.example.Elfagr.Order.Entity.Order;
import com.example.Elfagr.Return.Entity.Return;
import com.example.Elfagr.User.Enum.Gender;
import com.example.Elfagr.User.Enum.Role;
import com.example.Elfagr.User.Enum.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_name", columnList = "name"),
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phone")
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false,name = "name")
    private String name;

    @NotBlank
    @Column(nullable = false,unique = true,name = "email")
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @NotBlank
    @Column(nullable = false,unique = true,length = 20,name = "phone")
    private String phone;

    @NotNull
    @Column(nullable = false)
    private LocalDate birthdate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String verificationCode;
    @NotNull
    @Column(nullable = false)
    private String address;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginDate;

    private LocalDateTime deletedAt;

    @JsonProperty("enabled")
    private Boolean isEnabled;

    @JsonProperty("deleted")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<InventoryTransaction> inventoryTransactions;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Order> orders;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Return> returns;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+role.name().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
