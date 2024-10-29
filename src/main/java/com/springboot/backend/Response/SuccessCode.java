package com.springboot.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    // 인증 관련 성공 응답
    SmsSendSuccess(200, "S001", "인증번호 발송 성공"),
    SmsVerificationSuccess(200, "S002", "인증번호 검증 완료!"),
    SmsVerificationException(200, "S003", "인증 실패: 잘못된 인증번호입니다."),

    // 회원가입 및 로그인 관련 성공 응답
    SignupSuccess(201, "U001", "사용자 정보 저장 성공!"),
    SigninSuccess(200, "U002", "로그인 성공"),
    LoginPasswordFailure(200, "U003", "로그인 실패: 비밀번호 인증에 실패하였습니다."),
    LoginUserNotFound(200, "U004", "로그인 실패: 회원 정보가 존재하지 않습니다."),

    // 사용자 존재 여부 확인 성공 응답 (db에 휴대폰 번호 존재)
    ValidPhonenumSuccess(200, "U003", "사용자 정보가 존재합니다."),
    
    // 게시글 작성 성공 응답
    EventBoardImageSuccess(200, "B001", "게시글 이미지 저장 성공"),
    EventBoardSaveSuccess(200, "B002", "게시글 내용 저장 성공");

    private final int status;
    private final String code;
    private final String message;

}
