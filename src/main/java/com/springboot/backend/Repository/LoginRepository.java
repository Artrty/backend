package com.springboot.backend.Repository;

import com.springboot.backend.Entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    Optional<Login> findByPhoneNumber(String phoneNumber);
}
