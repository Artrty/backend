package com.springboot.backend.Service;

import com.springboot.backend.Entity.PhoneNumCertification;
import com.springboot.backend.Repository.PhoneNumCertificationRepository;
import com.springboot.backend.Repository.SmsCertification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


// Sms 인증 번호 검증
@Service
@RequiredArgsConstructor
public class SmsCertificationService {
    private final SmsCertification smsCertificationRepository;

    // 인증 번호 검증
    private final PhoneNumCertificationRepository phoneNumCertificationRepository;

    // 인증 번호 검증
    public String verifySms(PhoneNumCertification requestDto) {
        if (!isVerify(requestDto)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
        return "인증 완료되었습니다.";
    }

    // 검증 로직
    private boolean isVerify(PhoneNumCertification requestDto) {
        PhoneNumCertification storedCertification = phoneNumCertificationRepository.findByPhoneNumber(requestDto.getPhoneNumber());
        return storedCertification != null && storedCertification.getRandomNumber().equals(requestDto.getRandomNumber());
    }
}
