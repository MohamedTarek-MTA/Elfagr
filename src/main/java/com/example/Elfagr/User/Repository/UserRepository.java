package com.example.Elfagr.User.Repository;

import com.example.Elfagr.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Page<User> findByNameContainingIgnoreCase(String name , Pageable pageable);
}
