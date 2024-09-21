package com.springboot.backend.Controller;

import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Service.CoolSmsService;
import com.springboot.backend.Service.SmsCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoolSmsService coolSmsService;

    @Autowired
    private SmsCertificationService smsCertificationService;

    @GetMapping("/")
    @ResponseBody
    public String main() {
        return "main";
    }

    private final RestTemplate restTemplate = new RestTemplate();

    //로그인, 회원가입
    @PostMapping("/signup")
    @ResponseBody
    public String signup(@RequestBody User user) {
        // 사용자 정보를 받아서 저장
        user.setPhoneVerified(false); // 휴대폰 확인 여부를 초기값으로 설정
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

        return "----------사용자 정보 저장 성공!----------\n" + smsResponse;
    }
}
