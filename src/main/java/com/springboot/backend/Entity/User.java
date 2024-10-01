package com.springboot.backend.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uuid; // 자동 생성되는 UUID (기본키)

    @Column(nullable = false, unique = true)
    private String phoneNumber; // 사용자 전화번호 (고유한 값)

    private String userName; // 사용자 이름
    private boolean phoneVerified; // 휴대폰 번호 확인 여부
    private String password; // 비밀번호


    @CreationTimestamp
    @Column(updatable = false) // 생성 후에는 업데이트되지 않도록 설정
    private LocalDateTime createdAt; // 계정 생성일 (자동 저장)

    // Getter, Setter
    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // createdAt은 자동으로 설정되므로 setter는 제공하지 않음
}