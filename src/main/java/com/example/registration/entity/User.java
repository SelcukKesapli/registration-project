package com.example.registration.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity  // Bu sınıf bir veritabanı tablosunu temsil eder
@Table(name = "users")  // Tablonun adı 'users' olacak
public class User {

    @Id  // Bu alan birincil anahtar
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID otomatik artar
    private Long id;

    @Column(nullable = false)  // Boş bırakılamaz
    private String name;

    @Column(nullable = false, unique = true)  // Aynı e-posta iki kez olamaz
    private String email;

    @Column(nullable = false)
    private String password;  // BCrypt ile şifrelenmiş olacak

    @Column(nullable = false)
    private boolean enabled = false;  // E-posta doğrulanmadan aktif olamaz

    @CreationTimestamp  // Otomatik kayıt tarihi eklenecek
    private LocalDateTime createdAt;

    // Getter ve Setter'lar (veriye erişim için zorunlu)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
