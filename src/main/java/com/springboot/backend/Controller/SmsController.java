package com.springboot.backend.Controller;

import com.springboot.backend.Entity.PhoneNumCertification;
import com.springboot.backend.Service.CoolSmsService;
//import net.nurigo.java_sdk.exceptions.CoolsmsException;  // CoolsmsException import
import com.springboot.backend.Service.SmsCertificationService;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;  // Map import

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private CoolSmsService coolSmsService;

    @Autowired
    private SmsCertificationService smsCertificationService;


    // 인증 번호 전송
    @PostMapping("/send")
    public String sendSms(@RequestBody Map<String, String> body) {
        String phoneNumber = body.get("phoneNumber");
        try {
            String generatedCode = coolSmsService.sendSms(phoneNumber);
            return "Generated verification code: " + generatedCode;
        } catch (CoolsmsException e) {
            e.printStackTrace();
            return "Failed to send SMS: " + e.getMessage();
        }
    }


    // 인증번호 검증 엔드포인트
    @PostMapping("/verify-sms")
    public String verifySms(@RequestBody PhoneNumCertification certificationDto) {
        try {
            // SMS 인증 로직 실행
            return smsCertificationService.verifySms(certificationDto);
        } catch (IllegalArgumentException e) {
            return "인증 실패: " + e.getMessage();
        }
    }


}
