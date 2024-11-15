package com.springboot.backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_reservation")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventReservation { // 공연 예약 (참여 신청)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id") // 예약한 공연 id (EventBoard entity에서 join)
    private EventBoard event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 공연 예약한 예약자 user_id (User entity에서 join)
    private User user; // 사용자 이름 및 전화번호를 전달받을 예정

    @Column
    private boolean paymentStatus; // 공연비 입금 여부

    @CreationTimestamp
    private LocalDateTime rsvTime; // 예약 신청 날짜

    @Column
    private boolean rsvConfirmed; // 예약 성공 여부

    @Column
    private boolean rsvCanceled; // 예약 취소 여부

}
