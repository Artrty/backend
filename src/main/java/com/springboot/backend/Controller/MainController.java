package com.springboot.backend.Controller;

import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @GetMapping("/")
    @ResponseBody
    public String main() {
        return "main";
    }

    @Autowired
    private UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/signup")
    @ResponseBody
    public String signup(@RequestBody User user) {
        // 사용자 정보를 받아서 저장
        user.setPhoneVerified(false); // 휴대폰 확인 여부를 초기값으로 설정
        userRepository.save(user);

        // SMS 전송 API 호출
        String phoneNumber = user.getPhoneNumber();
        String smsResponse = sendSms(phoneNumber);  // SmsController의 API를 호출

        return "----------사용자 정보 저장 성공!----------\n" + smsResponse;
    }

    // RestTemplate을 사용하여 SMS API 호출
    private String sendSms(String phoneNumber) {
        String smsApiUrl = "http://localhost:8080/api/sms/send"; // SmsController의 URL

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, String> body = new HashMap<>();
        body.put("phoneNumber", phoneNumber);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(smsApiUrl, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();  // 성공 시 SMS 컨트롤러에서 반환하는 메시지
        } catch (Exception e) {
            e.printStackTrace();
            return "SMS 전송 실패: " + e.getMessage();
        }
    }
}
