package com.springboot.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid; // 기본키

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @Column(nullable = false, unique = true)
    private String phoneNumber;


    @NotBlank(message = "사용자 이름은 필수 항목입니다.")
    @Column(nullable = false)
    private String userName; // 사용자 이름

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Column(nullable = false)
    private String password; // 비밀번호

    @CreationTimestamp
    @Column(updatable = false) // 생성 후에는 업데이트되지 않도록 설정
    private LocalDateTime createdAt; // 계정 생성일 (자동 저장)

    // Getter, Setter
    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
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