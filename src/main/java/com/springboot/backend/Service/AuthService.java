package com.springboot.backend.Service;

import com.springboot.backend.Entity.Login;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.jwt.JwtTokenProvider;
import com.springboot.backend.jwt.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//로그인
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> signin(Login loginRequest) {
        String phoneNumber = loginRequest.getPhoneNumber();
        String password = loginRequest.getPassword();

        // 사용자 정보 조회
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자가 존재하지 않습니다.");
        }

        // 비밀번호 확인 (비밀번호는 암호화 - BCrypt로 비교)
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());

        // 비밀번호 일치 여부 로그 출력
        System.out.println("비밀번호 일치 여부: " + passwordMatch);

        if (!passwordMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
        }

        // 전화번호 인증 여부 확인
        if (!user.isPhoneVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("휴대폰 인증이 완료되지 않았습니다.");
        }

        // 로그인 성공 -> JWT 토큰 발급
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), null, new ArrayList<>());
        TokenInfo tokenInfo = jwtTokenProvider.createToken(authentication);

        return ResponseEntity.ok(new AuthResponse("로그인 성공!", tokenInfo.getAccessToken()));
    }
}
