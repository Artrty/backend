package com.springboot.backend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sms_certification")
public class PhoneNumCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 엔티티의 고유 ID

    @Column(nullable = false, unique = true)
    private String phoneNumber; // 휴대전화 번호

    @Column(nullable = false)
    private String randomNumber; // 인증번호

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

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }
}
