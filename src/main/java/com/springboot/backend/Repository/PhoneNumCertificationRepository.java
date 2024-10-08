package com.springboot.backend.Repository;

import com.springboot.backend.Entity.PhoneNumCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumCertificationRepository extends JpaRepository<PhoneNumCertification, Long> {
    PhoneNumCertification findByPhoneNumber(String phoneNumber);

    // 기존에 발급한 인증번호 무효화 (가장 최근에 전송 받은 인증번호만 유효)
    @Modifying
    @Query("UPDATE PhoneNumCertification p SET p.verifiedNumber = '' WHERE p.phoneNumber = :phoneNumber")
    void clearVerifiedNumbersForPhoneNumber(@Param("phoneNumber") String phoneNumber);

    // 가장 최근의 인증번호 가져오기
    PhoneNumCertification findFirstByPhoneNumberOrderByCreatedAtDesc(String phoneNumber);

    // 해당 번호의 모든 인증 데이터 삭제
    void deleteByPhoneNumber(String phoneNumber);
}
