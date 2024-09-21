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
    private String verifiedNumber; // 인증번호 (시스템)

    @Column
    private String  userVerifiedNumber; // 인증번호 (사용자 입력)
    // => 시스템에서 보낸 인증번호와 사용자가 저장한 인증번호가 동일한 경우 User에 있는 phoneVerified의 값을 1로 변경 후 verfiedNumber와 userVerifiedNumber 값을 삭제
    // => 만약 일치하지 않을 경우 userVerifiedNumber만 삭제

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
}
