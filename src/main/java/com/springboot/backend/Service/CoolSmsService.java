package com.springboot.backend.Service;

import com.springboot.backend.Entity.PhoneNumCertification;
import com.springboot.backend.Repository.PhoneNumCertificationRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CoolSmsService {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    private final PhoneNumCertificationRepository phoneNumCertificationRepository;

    public String sendSms(String to) throws CoolsmsException {
        try {
            // 랜덤한 4자리 인증번호 생성
            String numStr = generateRandomNumber();

            Message coolsms = new Message(apiKey, apiSecret);

            HashMap<String, String> params = new HashMap<>();
            params.put("to", to);    // 수신 전화번호
            params.put("from", fromPhoneNumber);    // 발신 전화번호
            params.put("type", "sms");
            params.put("text", "인증번호는 [" + numStr + "] 입니다.");

            // 메시지 전송
            JSONObject response = coolsms.send(params);

            if (response.containsKey("success_count") && ((Long) response.get("success_count")) > 0) {
                // 전화번호에 해당하는 기존 인증번호 레코드를 조회
                Optional<PhoneNumCertification> existingCertification = phoneNumCertificationRepository.findByPhoneNumber(to);

                if (existingCertification.isPresent()) {
                    // 이미 발송된 인증번호가 있으면 인증번호와 생성시간을 최신화
                    PhoneNumCertification certification = existingCertification.get();
                    certification.setVerifiedNumber(numStr); // 새로운 인증번호
                    certification.setCreatedAt(LocalDateTime.now()); // 현재 시간으로 업데이트
                    phoneNumCertificationRepository.save(certification); // DB에 업데이트
                } else {
                    // 기존에 발송된 인증번호가 없으면 새로 생성
                    PhoneNumCertification certification = new PhoneNumCertification();
                    certification.setPhoneNumber(to);
                    certification.setVerifiedNumber(numStr);
                    certification.setCreatedAt(LocalDateTime.now());

                    phoneNumCertificationRepository.save(certification);  // DB에 저장
                }

                return numStr; // 생성된 인증번호 반환
            } else {
                throw new CoolsmsException("Failed to send SMS: " + response.toString(), -1);
            }
        } catch (CoolsmsException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CoolsmsException("Failed to send SMS due to an unexpected error", -1);
        }
    }

    // 랜덤한 4자리 숫자 생성 메서드
    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }
}
