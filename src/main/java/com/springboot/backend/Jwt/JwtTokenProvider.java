package com.springboot.backend.Jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final String secretKey;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // token 만료 1일

    public JwtTokenProvider(@Value("${springboot.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    // SecretKey 반환
    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // jwt token 생성
    public TokenInfo createToken(Authentication authentication) {
        System.out.println("jwt token 생성");

        // Authentication 객체에서 CustomUser를 가져옴
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        // 사용자 권한 추출
        String authorities = customUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 토큰 생성
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        // JWT 생성
        String jwt;
        try {
            jwt = Jwts.builder()
                    .setSubject(customUser.getUsername()) // phoneNumber 사용
                    .claim("auth", authorities)
                    .claim("userId", customUser.getUserId())
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(getKey(), SignatureAlgorithm.HS256) // Key 서명
                    .compact();

            System.out.println("JWT 토큰 생성 완료: " + jwt);
        } catch (Exception e) {
            System.out.println("JWT 생성 중 예외 발생: " + e.getMessage());
            throw e; // 예외 다시 던짐
        }

        return new TokenInfo("Bearer", jwt);
    }



    // jwt token에서 Authentication 정보 추출
    public Authentication getAuthentication(String jwt) {
        System.out.println("jwt token에서 Authentication 정보 추출");
        Claims claims = getClaims(jwt);

        // 'auth' claim에서 권한 정보를 추출
        String auth = Optional.ofNullable(claims.get("auth", String.class))
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));

        // 'userId' claim에서 userId 정보를 추출
        Long userId = Optional.ofNullable(claims.get("userId", Long.class))
                .orElseThrow(() -> new RuntimeException("잘못된 토큰입니다."));

        // 권한 정보를 SimpleGrantedAuthority 객체로 변환
        Collection<GrantedAuthority> authorities = Arrays.stream(auth.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // CustomUser 객체 생성
        UserDetails principal = new CustomUser(userId, claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // token 검증
    public boolean validateToken(String token) {
        System.out.println("token 검증");
        try {
            getClaims(token);
            logger.info("JWT token이 유효하지 않음.");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT token 검증 실패: {}", e.getMessage());
            throw new JwtException("잘못된 토큰입니다.");
        }
    }

    private Claims getClaims(String jwt) {
        // JWT 토큰을 파싱하여 claims를 추출
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
