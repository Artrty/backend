package com.springboot.backend.Repository;

import com.springboot.backend.Entity.PhoneNumCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumCertificationRepository extends JpaRepository<PhoneNumCertification, Long> {
    PhoneNumCertification findByPhoneNumber(String phoneNumber);
    void deleteByPhoneNumber(String phoneNumber);
}
