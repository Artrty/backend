package com.springboot.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 인증 관련 오류
    SmsSendException(500, "S001", "SMS 전송 실패"),
    SmsVerificationException(400, "S002", "인증 실패: 잘못된 인증번호입니다."),
    SmsServerException(500, "S003", "인증 에러: 서버 오류로 인증이 실패했습니다."),

    // 회원가입 및 로그인 관련 오류
    UserExistsException(409, "U001", "이미 존재하는 사용자입니다."),
    LoginException(401, "U002", "로그인 실패: 잘못된 자격 증명입니다."),

    // 사용자 존재 여부 확인 오류 (db에 휴대폰 번호가 존재하지 않음)
    InvalidPhonenumException(404, "U003", "사용자 정보가 존재하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;

}


