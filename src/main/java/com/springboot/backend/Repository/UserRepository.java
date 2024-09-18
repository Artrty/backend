package com.springboot.backend.Repository;

import com.springboot.backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 전화번호로 사용자 조회
    User findByPhoneNumber(String phoneNumber);
}
