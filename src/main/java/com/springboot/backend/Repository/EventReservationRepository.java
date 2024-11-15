package com.springboot.backend.Repository;

import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Entity.EventReservation;
import com.springboot.backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventReservationRepository extends JpaRepository<EventReservation, Long> {
    List<EventReservation> findByUser(User user); // 특정 사용자 예약 조회
    List<EventReservation> findByEvent(EventBoard event); // 특정 공연 예약 조회
}
