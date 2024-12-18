package com.springboot.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 인증 관련 오류
    SmsSendException(500, "S501", "SMS 전송 실패"),
    SmsVerificationNumExpiredException(400, "S503", "인증 실패: 인증번호가 만료되었습니다."),
    SmsServerException(500, "S504", "인증 에러: 서버 오류로 인증이 실패했습니다."),

    // 회원가입 및 로그인 관련 오류
    UserExistsException(409, "U501", "이미 존재하는 사용자입니다."),
    LoginException(401, "U502", "로그인 실패: 잘못된 자격 증명입니다."),
    JwtCreationException(500, "U503", "JWT 생성 실패"),

    // 사용자 존재 여부 확인 오류 (db에 휴대폰 번호가 존재하지 않음)
    InvalidPhonenumException(404, "U504", "사용자 정보가 존재하지 않습니다."),
    
    // 게시글 작성 관련 오류
    FileCreationFailedException(500, "B501", "게시글 이미지 저장 실패: File 객체 생성 실패"),
    FileDeletionFailedException(500, "B502", "게시글 이미지 삭제 실패: 임시로 저장된 이미지 파일 삭제 실패"),
    FileConversionFailedException(500, "B503", "파일 변환 실패: MultipartFile -> File 전환 실패"),

    // 게시글 조회, 수정, 삭제 오류 응답
    EventBoardLoadException(404, "B504", "게시글 조회 실패 : 해당 ID에 대한 게시물 데이터가 존재하지 않습니다."),

    // 공연 예약 응답
    ReservationUserNotFound(404, "R501", "예약 생성 실패: 해당 사용자의 정보를 찾을 수 없습니다."),
    ReservationEventNotFound(404, "R502", "예약 생성 실패: 해당 공연의 정보를 찾을 수 없습니다."),
    ReservationServerError(500, "R503", "공연 예약 실패: 서버 오류 발생"),

    // 공연 예약 정보 조회 응답
    ReservationInfoNotFound(404, "R504", "조회 실패: 해당 사용자에 대한 예약 정보가 존재하지 않습니다."),
    ReservationInfoLoadServerError(500, "R505", "예약 정보 조회 실패: 서버 오류 발생");
    
    private final int status;
    private final String code;
    private final String message;

}


