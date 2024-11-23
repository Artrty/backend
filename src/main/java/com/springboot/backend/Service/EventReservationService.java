package com.springboot.backend.Service;

import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Entity.EventReservation;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.EventBoardRepository;
import com.springboot.backend.Repository.EventReservationRepository;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Response.ErrorCode;
import com.springboot.backend.Response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventReservationService {

    private final EventReservationRepository eventReservationRepository;
    private final UserRepository userRepository;
    private final EventBoardRepository eventBoardRepository;

    // 공연 예약 생성 (EventBoard ID 사용)
    public EventReservation createReservationByEventId(Long eventId, Long userId, EventReservation reservation) {
        System.out.println("공연 예약 생성");

        if (eventId == null || userId == null) {
            throw new IllegalArgumentException();
        }

        EventBoard event = eventBoardRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ReservationEventNotFound.getMessage()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ReservationUserNotFound.getMessage()));

        // 중복 예약 체크
        boolean isDuplicate = eventReservationRepository.findByUser(user).stream()
                .anyMatch(existingReservation -> existingReservation.getEvent().getId().equals(event.getId()) && !existingReservation.isRsvCanceled());
        if (isDuplicate) {
            throw new IllegalArgumentException(SuccessCode.ReservationDuplicate.getMessage());
        }

        // 예약 생성
        reservation.setEvent(event);
//        reservation.setUser(user);
        reservation.setRsvTime(LocalDateTime.now());
        reservation.setPaymentStatus(false);
        reservation.setRsvConfirmed(false);
        reservation.setRsvCanceled(false);

        return eventReservationRepository.save(reservation);
    }

    // 사용자별 예약 목록 조회
    public List<EventReservation> getReservationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ReservationInfoNotFound.getMessage()));

        List<EventReservation> reservations = eventReservationRepository.findActiveReservationsByUser(user.getUuid());
        if (reservations.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.ReservationInfoNotFound.getMessage());
        }

        return reservations;
    }
}


