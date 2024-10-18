package com.springboot.backend.Repository;

import com.springboot.backend.Entity.PhoneNumCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneNumCertificationRepository extends JpaRepository<PhoneNumCertification, Long> {
    Optional<PhoneNumCertification> findByPhoneNumber(String phoneNumber);

    // 해당 번호의 모든 인증 데이터 삭제
    void deleteByPhoneNumber(String phoneNumber);
}
