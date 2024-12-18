package com.springboot.backend.Controller;

import com.springboot.backend.Dto.EventReservationRequest;
import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Entity.EventReservation;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Response.ApiResponse;
import com.springboot.backend.Response.ErrorCode;
import com.springboot.backend.Response.Exception.EventBoardLoadException;
import com.springboot.backend.Response.Exception.InvalidPhonenumException;
import com.springboot.backend.Response.SuccessCode;
import com.springboot.backend.Service.EventReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event-rsv")
@RequiredArgsConstructor
public class EventReservationController {

    private final EventReservationService eventReservationService;

    // 공연 예약 신청 API
    @PostMapping("/{id}/reserve")
    public ResponseEntity<ApiResponse<?>> postEventReservation(@PathVariable Long id, @RequestBody EventReservationRequest request) {
        System.out.println("공연 예약 시작");
        try {
            EventReservation reservation = new EventReservation();
            reservation.setPaymentStatus(request.isPaymentStatus());
            reservation.setRsvConfirmed(request.isRsvConfirmed());
            reservation.setRsvCanceled(request.isRsvCanceled());

            EventReservation createdReservation = eventReservationService.createReservationByEventId(id, request.getUserId(), reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.successResponse(SuccessCode.ReservationSuccess, createdReservation)
            );
        } catch (IllegalArgumentException e) { // 예약 실패 - 사용자 조회 실패
            return buildErrorResponse(ErrorCode.ReservationUserNotFound);
        // 예약 실패 - 중복 예약(이미 예약된 경우)
        } catch (Exception e) { // 예약 실패 - 서버 오류
            e.printStackTrace();
            return buildErrorResponse(ErrorCode.ReservationServerError);
        }
    }



    // 사용자 ID에 대한 공연 예약 정보 조회 API (예약 신청 상태)
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getReservationsByUser(@PathVariable Long userId) {
        try {
            List<EventReservation> reservations = eventReservationService.getReservationsByUser(userId);
            return ResponseEntity.ok( // 예약 정보 조회 성공
                    ApiResponse.successResponse(SuccessCode.ReservationInfoLoadSuccess, reservations)
            );
//        } catch (InvalidPhonenumException e) { // 예약 정보 조회 실패 - 정보 조회 실패
//            return buildErrorResponse(ErrorCode.ReservationInfoNotFound);
        } catch (Exception e) { // 예약 정보 조회 실패 - 서버 오류
            return buildErrorResponse(ErrorCode.ReservationInfoLoadServerError);
        }
    }

    // 에러 응답 빌더 메서드
    private ResponseEntity<ApiResponse<?>> buildErrorResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(
                ApiResponse.errorResponse(errorCode)
        );
    }

    // 사용자 ID에 대한 공연 예약 정보 조회 API (예약 완료 상태)

}




