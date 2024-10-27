package com.springboot.backend.Service;

import com.springboot.backend.Entity.PhoneNumCertification;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.PhoneNumCertificationRepository;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Response.ApiResponse;
import com.springboot.backend.Response.ErrorCode;
import com.springboot.backend.Response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsCertificationService {

    private final PhoneNumCertificationRepository phoneNumCertificationRepository;
    private final UserRepository userRepository;

    // 인증번호 검증
    @Transactional
    public ApiResponse<String> verifySms(PhoneNumCertification requestDto) {
        System.out.println("인증번호 검증");

        // 해당 전화번호로 발급된 인증번호 가져오기
        Optional<PhoneNumCertification> optionalCertification = phoneNumCertificationRepository
                .findByPhoneNumber(requestDto.getPhoneNumber());

        // 인증 기록이 없으면 예외 처리
        PhoneNumCertification storedCertification = optionalCertification
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.InvalidPhonenumException.getMessage()));

        // 현재 시간과 인증번호 생성 시간 비교
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = storedCertification.getCreatedAt();

        // 3분 이내에만 인증 가능
        if (Duration.between(createdAt, now).toMinutes() > 3) {
            // 유효 시간이 지났다면 해당 데이터 삭제
            phoneNumCertificationRepository.deleteByPhoneNumber(requestDto.getPhoneNumber());
            return (ApiResponse<String>) ApiResponse.errorResponse(ErrorCode.SmsVerificationNumExpiredException);
        }

        // 사용자가 입력한 인증번호 검증
        if (storedCertification.getVerifiedNumber().equals(requestDto.getUserVerifiedNumber())) {

            // 인증이 완료되었으므로 PhoneNumCertification에서 인증 기록 삭제
            phoneNumCertificationRepository.deleteByPhoneNumber(requestDto.getPhoneNumber());

            // 인증 완료 성공 응답 반환
            return (ApiResponse<String>) ApiResponse.successResponse(SuccessCode.SmsVerificationSuccess, "인증 완료되었습니다.");
        } else {
            // 인증번호가 일치하지 않을 경우
            return (ApiResponse<String>) ApiResponse.successResponse(SuccessCode.SmsVerificationException, null);
        }
    }
}
