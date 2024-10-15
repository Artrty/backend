package com.springboot.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_certification")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class PhoneNumCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 엔티티의 고유 ID

    @Column(nullable = false)
    private String phoneNumber; // 휴대전화 번호

    @Column(nullable = false)
    private String verifiedNumber; // 인증번호 (시스템)

    @Column
    private String  userVerifiedNumber; // 인증번호 (사용자 입력)

    @Column(nullable = false)
    private LocalDateTime createdAt; // 인증번호 생성 시간

    // 새로운 인증번호 재발급
    public void regenerateVerifiedNumber(String newVerifiedNumber) {
        this.verifiedNumber = newVerifiedNumber;
        this.createdAt = LocalDateTime.now(); // 재발급 시 생성 시간 업데이트
    }
}
