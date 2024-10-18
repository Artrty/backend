package com.springboot.backend.Service;

import com.springboot.backend.Entity.Login;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Jwt.CustomUser;
import com.springboot.backend.Jwt.JwtTokenProvider;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Response.ApiResponse;
import com.springboot.backend.Response.AuthResponse;
import com.springboot.backend.Response.SuccessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 로그인 메서드
    public ResponseEntity<ApiResponse<?>> signin(Login loginRequest) {
        System.out.println("로그인 - 사용자 정보 조회");
        String phoneNumber = loginRequest.getPhoneNumber();
        String password = loginRequest.getPassword();

        // 사용자 정보 조회
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.LoginUserNotFound, null));
        }

        // 비밀번호 확인 (비밀번호는 암호화 - BCrypt로 비교)
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());

        // 비밀번호 일치 여부 로그 출력
        System.out.println("비밀번호 일치 여부: " + passwordMatch);

        if (!passwordMatch) {
            return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.LoginPasswordFailure, null));
        }

        // CustomUser 객체 생성
        CustomUser customUser = new CustomUser(user.getUuid(), user.getPhoneNumber(), user.getPassword(), getAuthorities(user));

        // UsernamePasswordAuthenticationToken으로 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());

        // JWT 토큰 생성 (Authentication 객체 전달)
        String token = jwtTokenProvider.createToken(authentication).getAccessToken();

        // AuthResponse 객체 생성
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);

        // 응답 데이터 설정
        Map<String, Object> data = new HashMap<>();
        data.put("authResponse", authResponse);

        System.out.println("로그인 성공");
        // 로그인 성공 응답
        return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.SigninSuccess, data));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
