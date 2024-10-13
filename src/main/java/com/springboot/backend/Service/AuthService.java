package com.springboot.backend.Service;

import com.springboot.backend.Entity.Login;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Jwt.CustomUser;
import com.springboot.backend.Jwt.JwtTokenProvider;
import com.springboot.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
    public Authentication signin(Login loginRequest) {
        System.out.println("로그인 - 사용자 정보 조회");
        String phoneNumber = loginRequest.getPhoneNumber();
        String password = loginRequest.getPassword();

        // 사용자 정보 조회
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        // 비밀번호 확인 (비밀번호는 암호화 - BCrypt로 비교)
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());

        // 비밀번호 일치 여부 로그 출력
        System.out.println("비밀번호 일치 여부: " + passwordMatch);

        if (!passwordMatch) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // CustomUser 객체 생성
        CustomUser customUser = new CustomUser(user.getUuid(), user.getPhoneNumber(), user.getPassword(), getAuthorities(user));

        // Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
