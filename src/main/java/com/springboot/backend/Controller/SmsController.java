package com.springboot.backend.Controller;

import com.springboot.backend.Entity.Login;
import com.springboot.backend.Entity.PhoneNumCertification;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Jwt.JwtTokenProvider;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Response.ApiResponse;
import com.springboot.backend.Response.ErrorCode;
import com.springboot.backend.Response.SuccessCode;
import com.springboot.backend.Service.AuthService;
import com.springboot.backend.Service.CoolSmsService;
import com.springboot.backend.Service.SmsCertificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 인증번호 발송
    @GetMapping("/{phoneNumber}/send-sms")
    @Operation(summary = "Send SMS Code", description = "사용자의 전화번호로 SMS 인증번호를 발송합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "SMS 인증번호 발송 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": null, \"message\": \"인증번호 발송 성공\", \"code\": \"S001\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "인증번호 재발송 불가",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": null, \"message\": \"인증번호 재발송은 3분 후에 가능합니다.\", \"code\": \"S502\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "SMS 인증번호 발송 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": null, \"message\": \"SMS 전송 실패\", \"code\": \"S501\"}")))
    })
    public ResponseEntity<ApiResponse<?>> sendSms(@PathVariable String phoneNumber) {
        System.out.println("SMS 인증번호 발송 시도: " + phoneNumber);

        // SMS 재발송 가능 여부 체크
        if (smsCertificationService.canSendSms(phoneNumber)) {
            try {
                String randomNumber = coolSmsService.sendSms(phoneNumber);
                System.out.println("SMS 인증번호 발송 성공: " + randomNumber);
                return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.SmsSendSuccess, null));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.errorResponse(ErrorCode.SmsSendException));
            }
        } else { // 인증번호 재발송 대기 메세지 출력
            return ResponseEntity.badRequest()
                    .body(ApiResponse.successResponse(SuccessCode.SmsVerificationResendNotice, null));
        }
    }

    // 인증번호 검증
    @PostMapping("/verify-sms")
    @Operation(summary = "Verify SMS Code", description = "SMS 인증번호를 검증합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증 성공 또는 인증번호 만료",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "인증 성공", value = "{\"data\": {\"user\": {\"id\": 123, \"name\": \"홍길동\", \"phoneNumber\": \"010-1234-5678\"}}, \"message\": \"인증번호 검증 완료\", \"code\": \"S002\"}"),
                                    @ExampleObject(name = "인증 실패", value = "{\"data\": null, \"message\": \"인증 실패: 잘못된 인증번호입니다.\", \"code\": \"S003\"}")
                            }
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "인증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": null, \"message\": \"인증번호 만료\", \"code\": \"S501\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류로 인한 인증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": null, \"message\": \"SMS 전송 실패\", \"code\": \"S503\"}")))
    })
    public ResponseEntity<ApiResponse<?>> verifySms(@RequestBody PhoneNumCertification certificationDto) {
        System.out.println("인증번호 검증 엔드포인트");
        try {
            // SMS 인증 로직 실행
            ApiResponse<String> result = smsCertificationService.verifySms(certificationDto);

            Map<String, Object> data = new HashMap<>();

            // 인증 성공
            if ("인증 완료되었습니다.".equals(result.getData())) {
                // 인증 성공한 사용자의 정보를 가져옴
                User user = userRepository.findByPhoneNumber(certificationDto.getPhoneNumber());

                if (user != null) {
                    // 사용자 정보를 data에 추가, 비밀번호는 제거
                    data.put("user", sanitizeUser(user));
                }

                return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.SmsVerificationSuccess, data));
            } else {
                // 인증 실패
                return ResponseEntity.badRequest()
                        .body(ApiResponse.successResponse(SuccessCode.SmsVerificationException, null));
            }
        } catch (IllegalArgumentException e) { // 인증번호 만료
            return ResponseEntity.badRequest()
                    .body(ApiResponse.errorResponse(ErrorCode.SmsServerException));
        }
    }

    // 회원가입
    @PostMapping("/signup")
    @Operation(summary = "User Signup", description = "신규 사용자를 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": {\"user\": {\"id\": 123, \"name\": \"홍길동\"}}, \"message\": \"사용자 정보 저장 성공\", \"code\": \"U001\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 존재하는 사용자",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": null, \"message\": \"이미 존재하는 사용자입니다.\", \"code\": \"U501\"}")))
    })
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody User user) {
        System.out.println("회원가입 진행");
        // 전화번호로 유저가 이미 존재하는지 확인
        User existingUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (existingUser != null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.errorResponse(ErrorCode.UserExistsException));
        }

        // 입력 받은 비밀번호를 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);// 암호화된 비밀번호 저장

        // 사용자 정보 저장
        userRepository.save(user);

        // 사용자 정보와 메시지를 나누어 응답
        Map<String, Object> data = new HashMap<>();
        data.put("user", sanitizeUser(user)); // 민감한 정보 제거

        // 회원가입 성공 응답
        return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.SignupSuccess, data));
    }

    // 비밀번호를 포함한 민감한 정보를 제거한 User 객체 반환
    private User sanitizeUser(User user) {
        user.setPassword(null); // 비밀번호는 null로 설정하여 제외
        return user;
    }

    @PostMapping("/signin")
    @Operation(summary = "User Signin", description = "로그인을 진행합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": {\"token\": \"jwt-token\"}, \"message\": \"로그인 성공\", \"code\": \"U002\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": null, \"message\": \"로그인 실패: 잘못된 자격 증명입니다.\", \"code\": \"U502\"}")))
    })
    public ResponseEntity<ApiResponse<?>> signin(@RequestBody Login loginRequest) {

        // AuthService의 signin 메서드를 호출하고 결과 반환
        return authService.signin(loginRequest);
    }
}
