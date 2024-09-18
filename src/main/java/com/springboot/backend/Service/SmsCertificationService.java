package com.springboot.backend.Service;

import com.springboot.backend.Entity.PhoneNumCertification;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.PhoneNumCertificationRepository;
import com.springboot.backend.Repository.SmsCertification;
import com.springboot.backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SmsCertificationService {
    private final SmsCertification smsCertificationRepository;
    private final PhoneNumCertificationRepository phoneNumCertificationRepository;
    private final UserRepository userRepository;

    // 인증 번호 검증
    @Transactional
    public String verifySms(PhoneNumCertification requestDto) {
        // Redis에 저장된 인증번호 가져오기
        String storedCertification = smsCertificationRepository.getSmsCertification(requestDto.getPhoneNumber());

        if (storedCertification == null || !storedCertification.equals(requestDto.getVerifiedNumber())) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        // 인증 성공: PhoneNumCertification에 인증번호 저장
        phoneNumCertificationRepository.save(requestDto);

        // User의 phone_verified를 true로 변경
        User user = userRepository.findByPhoneNumber(requestDto.getPhoneNumber());
        if (user != null) {
            user.setPhoneVerified(true); // 휴대폰 인증 완료
            userRepository.save(user);  // 변경사항 저장
        } else {
            throw new IllegalArgumentException("해당 번호의 사용자를 찾을 수 없습니다.");
        }

        // Redis에서 인증번호 삭제
        smsCertificationRepository.deleteSmsCertification(requestDto.getPhoneNumber());

        return "인증 완료되었습니다.";
    }
}
