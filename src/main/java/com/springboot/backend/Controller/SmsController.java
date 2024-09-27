package com.springboot.backend.Controller;

import com.springboot.backend.Entity.Login;
import com.springboot.backend.Entity.PhoneNumCertification;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Service.CoolSmsService;
import com.springboot.backend.Service.SmsCertificationService;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SmsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoolSmsService coolSmsService;

    @Autowired
    private SmsCertificationService smsCertificationService;

    // 회원가입
    @PostMapping("/signup")
    @ResponseBody
    public String signup(@RequestBody User user) {
        // 전화번호로 유저가 이미 존재하는지 확인
        User existingUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (existingUser != null) {
            return "이미 존재하는 사용자입니다.";
        }

        // 사용자 정보를 받아서 저장
        user.setPhoneVerified(false); // 휴대폰 인증 여부를 초기값으로 설정
        userRepository.save(user);

        // 인증번호 생성 및 저장
        String phoneNumber = user.getPhoneNumber();
        String smsResponse;
        try {
            String randomNumber = coolSmsService.sendSms(phoneNumber); // 인증번호 생성 및 SMS 전송
            smsResponse = "인증번호가 SMS로 발송되었습니다.";
        } catch (Exception e) {
            smsResponse = "SMS 전송 실패: " + e.getMessage();
        }

        return "---------- 사용자 정보 저장 성공! ----------" + smsResponse;
    }

    // 인증번호 검증 엔드포인트
    @PostMapping("/verify-sms")
    public String verifySms(@RequestBody PhoneNumCertification certificationDto) {
        try {
            // SMS 인증 로직 실행
            String result = smsCertificationService.verifySms(certificationDto);

            // 인증이 성공하면 특정 메시지를 반환
            if ("인증 완료되었습니다.".equals(result)) {
                return "----------인증번호 검증 완료!----------\n";
            } else {
                return "인증 실패: " + result;
            }
        } catch (IllegalArgumentException e) {
            return "인증 에러: " + e.getMessage();
        }
    }

    // 로그인
    @PostMapping("/signin")
    @ResponseBody
    public String signin(@RequestBody Login loginRequest) {
        String phoneNumber = loginRequest.getPhoneNumber();
        String password = loginRequest.getPassword();

        // 사용자 정보 조회
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return "사용자가 존재하지 않습니다.";
        }

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            return "비밀번호가 틀렸습니다.";
        }

        // 전화번호 인증 여부 확인
        if (!user.isPhoneVerified()) {
            return "휴대폰 인증이 완료되지 않았습니다.";
        }

        return "로그인 성공!";
    }

}