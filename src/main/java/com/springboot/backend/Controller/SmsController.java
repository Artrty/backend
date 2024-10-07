package com.springboot.backend.Controller;

import com.springboot.backend.Entity.Login;
import com.springboot.backend.Entity.PhoneNumCertification;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Response.ApiResponse;
import com.springboot.backend.Service.AuthService;
import com.springboot.backend.Service.CoolSmsService;
import com.springboot.backend.Service.SmsCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 비밀번호를 암호화하여 저장

    @Autowired
    private AuthService authService;

    // 인증번호 발송
    @GetMapping("/{phoneNumber}/send-sms")
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> sendSms(@PathVariable String phoneNumber) {
        // SMS 인증번호 발송
        try {
            String randomNumber = coolSmsService.sendSms(user.getPhoneNumber());
            return ResponseEntity.ok(ApiResponse.successWithNoContent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.errorResponse("SMS 전송 실패: " + e.getMessage()));
        }
    }

    // 회원가입
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody User user) {
        // 전화번호로 유저가 이미 존재하는지 확인
        User existingUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(ApiResponse.errorResponse("이미 존재하는 사용자입니다."));
        }

        // 입력 받은 비밀번호를 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);// 암호화된 비밀번호 저장
        user.setPhoneVerified(false); // 휴대폰 인증 여부를 초기값으로 설정

        // 사용자 정보 저장
        userRepository.save(user);

        // 사용자 정보와 메시지를 나누어 응답
        Map<String, Object> data = new HashMap<>();
        data.put("user", sanitizeUser(user)); // 민감한 정보 제거

        // 회원가입 성공 응답
        return ResponseEntity.ok(ApiResponse.successResponse(data, "사용자 정보 저장 성공! "));
    }

    // 비밀번호를 포함한 민감한 정보를 제거한 User 객체 반환
    private User sanitizeUser(User user) {
        user.setPassword(null); // 비밀번호는 null로 설정하여 제외
        return user;
    }

    // 인증번호 검증 엔드포인트
    @PostMapping("/verify-sms")
    public ResponseEntity<ApiResponse<?>> verifySms(@RequestBody PhoneNumCertification certificationDto) {
        try {
            // SMS 인증 로직 실행
            String result = smsCertificationService.verifySms(certificationDto);

            Map<String, Object> data = new HashMap<>();

            // 인증 성공
            if ("인증 완료되었습니다.".equals(result)) {
                // 인증 성공한 사용자의 정보를 가져옴
                User user = userRepository.findByPhoneNumber(certificationDto.getPhoneNumber());

                if (user != null) {
                    // 사용자 정보를 data에 추가, 비밀번호는 제거
                    data.put("user", sanitizeUser(user));
                }

                return ResponseEntity.ok(ApiResponse.successResponse(data, "인증번호 검증 완료!"));
            } else {
                // 인증 실패
                return ResponseEntity.badRequest().body(ApiResponse.errorResponse("인증 실패: " + result));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.errorResponse("인증 에러: " + e.getMessage()));
        }
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> signin(@RequestBody Login loginRequest) {
        ResponseEntity<?> authResponse = authService.signin(loginRequest);

        Map<String, Object> data = new HashMap<>();

        // authResponse.getBody()를 String으로 변환하여 메시지로 사용
        String message = authResponse.getBody() != null ? authResponse.getBody().toString() : "로그인 성공";

        if (authResponse.getStatusCode() == HttpStatus.OK) { // 로그인 성공
            return ResponseEntity.ok(ApiResponse.successResponse(data, message));
        } else { // 로그인 실패
            return ResponseEntity.status(authResponse.getStatusCode())
                    .body(ApiResponse.errorResponse("로그인 실패"));
        }
    }

}
