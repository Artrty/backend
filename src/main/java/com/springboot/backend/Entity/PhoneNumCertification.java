package com.springboot.backend.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_certification")
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

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerifiedNumber() {
        return verifiedNumber;
    }

    public void setVerifiedNumber(String verifiedNumber) {
        this.verifiedNumber = verifiedNumber;
    }

    public String getUserVerifiedNumber() {
        return userVerifiedNumber;
    }

    public void setUserVerifiedNumber(String userVerifiedNumber) {
        this.userVerifiedNumber = userVerifiedNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
