package com.springboot.backend.Service;

import com.springboot.backend.Entity.PhoneNumCertification;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.PhoneNumCertificationRepository;
import com.springboot.backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsCertificationService {

    private final PhoneNumCertificationRepository phoneNumCertificationRepository;
    private final UserRepository userRepository;


    // 인증 번호 검증
    @Transactional
    public String verifySms(PhoneNumCertification requestDto) {
        // DB에 저장된 인증번호 가져오기
        PhoneNumCertification storedCertification = phoneNumCertificationRepository.findByPhoneNumber(requestDto.getPhoneNumber());

        if (storedCertification == null) {
            throw new IllegalArgumentException("해당 번호의 인증 기록이 없습니다.");
        }

        // 현재 시간과 인증번호 생성 시간 비교
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = storedCertification.getCreatedAt();

        // 3분 이내에만 인증 가능
        if (Duration.between(createdAt, now).toMinutes() > 3) {
            // 유효 시간이 지났다면, 인증번호를 빈 값으로 설정
            storedCertification.clearVerifiedNumber();
            phoneNumCertificationRepository.save(storedCertification);

            return "인증번호의 유효 시간이 지났습니다. \n 인증번호를 재발급 받으세요.";
        }

        // 사용자가 입력한 인증번호 설정
        storedCertification.setUserVerifiedNumber(requestDto.getUserVerifiedNumber());

        // 인증번호 비교
        if (storedCertification.getVerifiedNumber().equals(storedCertification.getUserVerifiedNumber())) {
            // 인증 성공: User의 phoneVerified를 true로 변경
            User user = userRepository.findByPhoneNumber(requestDto.getPhoneNumber());
            if (user != null) {
                user.setPhoneVerified(true); // 휴대폰 인증 완료
                userRepository.save(user);  // 변경사항 저장
            } else {
                throw new IllegalArgumentException("해당 번호의 사용자를 찾을 수 없습니다.");
            }

            // 인증이 완료되었으므로 PhoneNumCertification에서 인증 기록 삭제
            phoneNumCertificationRepository.delete(storedCertification);

            return "인증 완료되었습니다.";
        } else {
            // 인증번호가 일치하지 않을 경우 userVerifiedNumber만 삭제
            storedCertification.setUserVerifiedNumber(null);
            phoneNumCertificationRepository.save(storedCertification);

            return "인증번호가 일치하지 않습니다.";
        }
    }
}
