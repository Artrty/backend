package com.springboot.backend.Service;

import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Entity.EventReservation;
import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.EventBoardRepository;
import com.springboot.backend.Repository.EventReservationRepository;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Response.ErrorCode;
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

    // 공연 예약 생성
    public EventReservation createReservation(EventReservation reservation) {
        User user = userRepository.findById(reservation.getUser().getUuid()) // 공연 예약 실패 - 사용자 조회 실패
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ReservationUserNotFound.getMessage()));

        EventBoard event = eventBoardRepository.findById(reservation.getEvent().getId())  // 공연 예약 실패 - 해당 공연 id 조회 실패
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ReservationEventNotFound.getMessage()));

        // 중복 예약 체크
        boolean isDuplicate = eventReservationRepository.findByUser(user).stream()
                .anyMatch(existingReservation -> existingReservation.getEvent().getId().equals(event.getId()) && !existingReservation.isRsvCanceled());
        if (isDuplicate) {
            throw new IllegalArgumentException(ErrorCode.ReservationEventNotFound.getMessage());
        }

        reservation.setUser(user);
        reservation.setEvent(event);
        reservation.setRsvTime(LocalDateTime.now());
        reservation.setRsvConfirmed(false); // 초기값 설정
        reservation.setRsvCanceled(false); // 초기값 설정

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


