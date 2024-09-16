package com.springboot.backend.Service;

//import net.nurigo.java_sdk.exceptions.CoolsmsException;  // CoolsmsException import
//import net.nurigo.java_sdk.api.Message;  // CoolSMS API의 Message 클래스 import
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;  // HashMap import
import java.util.Random;  // Random 클래스 import

@Service
public class CoolSmsService {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    public String sendSms(String to) throws CoolsmsException {
        try {
            // 랜덤한 4자리 인증번호 생성
            String numStr = generateRandomNumber();

            Message coolsms = new Message(apiKey, apiSecret); // 생성자를 통해 API 키와 API 시크릿 전달

            HashMap<String, String> params = new HashMap<>();
            params.put("to", to);    // 수신 전화번호
            params.put("from", fromPhoneNumber);    // 발신 전화번호
            params.put("type", "sms");
            params.put("text", "인증번호는 [" + numStr + "] 입니다.");

            // 메시지 전송
            JSONObject response = coolsms.send(params);

            // API 응답 로그
            System.out.println("API Response: " + response.toString());

            // 성공 여부를 확인
            if (response.containsKey("success_count") && ((Long) response.get("success_count")) > 0) {
                return numStr; // 생성된 인증번호 반환
            } else {
                throw new CoolsmsException("Failed to send SMS: " + response.toString(), -1);
            }

        } catch (CoolsmsException e) {
            System.err.println("CoolSmsException: " + e.getMessage());
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
