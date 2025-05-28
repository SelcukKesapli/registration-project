package com.example.registration.repository;

import com.example.registration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // E-posta ile kullanıcıyı bulmak için
    Optional<User> findByEmail(String email);

    // Aynı e-posta daha önce kullanılmış mı?
    boolean existsByEmail(String email);
}
