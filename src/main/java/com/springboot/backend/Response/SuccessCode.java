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
    SmsVerificationResendNotice(200, "S004", "인증번호 재전송 불가: 이전 요청으로부터 3분 후 재시도해 주시기 바랍니다."),

    // 회원가입 및 로그인 관련 성공 응답
    SignupSuccess(201, "U001", "사용자 정보 저장 성공!"),
    SigninSuccess(200, "U002", "로그인 성공"),
    LoginPasswordFailure(200, "U003", "로그인 실패: 비밀번호 인증에 실패하였습니다."),
    LoginUserNotFound(200, "U004", "로그인 실패: 회원 정보가 존재하지 않습니다."),

    // 사용자 존재 여부 확인 성공 응답 (db에 휴대폰 번호 존재)
    ValidPhonenumSuccess(200, "U003", "사용자 정보가 존재합니다."),

    // 게시글 작성 성공 응답
    EventBoardImageSuccess(200, "B001", "게시글 이미지 저장 성공"),
    EventBoardSaveSuccess(200, "B002", "게시글 저장 성공 : 관리자의 승인이 필요합니다."),
    ImageFileSizeCheckSuccess(200, "B003", "이미지 파일 용량 체크 성공"),
    ImageCountCheckSuccess(200, "B004", "이미지 개수 체크 성공"),
    ImageFileSizeExceeded(200, "B005", "이미지 재업로드 요청 : 이미지 파일 크기 초과"),
    ImageNumEsceeded(200, "B006", "이미지 재업로드 요청 : 이미지 개수 초과"),

    // 게시글 조회, 수정, 삭제 성공 응답
    EventBoardLoadSuccess(200, "B005", "게시글 조회 성공"),
    EventBoardUpdateSuccess(200, "B006", "게시글 수정 성공"),
    EventBoardUpdateFailed(200, "B007", "게시글 수정 실패 : 해당 ID에 대한 게시물이 존재하지 않음"),
    // 수정된 내용이 없습니다
    EventBoardDeleteSuccess(200, "B009", "게시글 삭제 성공"),
    EventBoardDeleteFailed(200, "B010", "게시글 삭제 실패 : 해당 ID에 대한 게시물이 존재하지 않음"),
    EventBoardAllLoadSuccess(200, "B011", "전체 게시글 조회 성공");

    private final int status;
    private final String code;
    private final String message;
}
