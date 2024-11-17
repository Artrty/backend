package com.springboot.backend.Repository;

import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Entity.EventReservation;
import com.springboot.backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventReservationRepository extends JpaRepository<EventReservation, Long> {

    // 사용자 ID를 기준으로 활성 상태의 예약만 조회
    @Query("SELECT r FROM EventReservation r " +
            "JOIN FETCH r.user u " +
            "JOIN FETCH r.event e " +
            "WHERE u.uuid = :userId AND r.rsvCanceled = false")
    List<EventReservation> findActiveReservationsByUser(@Param("userId") Long userId);

    List<EventReservation> findByUser(User user); // 특정 사용자 예약 조회
    List<EventReservation> findByEvent(EventBoard event); // 특정 공연 예약 조회
}
