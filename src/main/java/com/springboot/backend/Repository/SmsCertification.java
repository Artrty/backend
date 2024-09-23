//package com.springboot.backend.Repository;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Repository;
//import java.time.Duration;
//
//@Repository
//@RequiredArgsConstructor
//public class SmsCertification {
//    private final String PREFIX = "sms:";
//    private final int LIMIT_TIME = 3 * 60; // 인증번호 유효 시간
//
//    private final StringRedisTemplate stringRedisTemplate;
//
//    // Redis에 저장
//    public void createSmsCertification(String phone, String certificationNumber) {
//        stringRedisTemplate.opsForValue()
//                .set(PREFIX + phone, certificationNumber, Duration.ofSeconds(LIMIT_TIME));
//    }
//
//    // Redis에서 인증번호 가져오기
//    public String getSmsCertification(String phone) {
//        return stringRedisTemplate.opsForValue().get(PREFIX + phone);
//    }
//
//    // Redis에서 인증번호 삭제
//    public void deleteSmsCertification(String phone) {
//        stringRedisTemplate.delete(PREFIX + phone);
//    }
//
//    // Redis에 해당 전화번호로 인증번호가 존재하는지 확인
//    public boolean hasKey(String phone) {
//        return stringRedisTemplate.hasKey(PREFIX + phone);
//    }
//}