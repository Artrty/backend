package com.springboot.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_board")
@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EventBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    @NotBlank(message = "공연 제목은 필수로 입력해야 합니다.")
    @Column(nullable = false)
    private String eventTitle; // 공연 제목

//    @Column(nullable = false)
//    private String eventArea; // 공연 지역

    @NotBlank(message = "공연 장소는 필수로 입력해야 합니다.")
    @Column(nullable = false)
    private String eventLocation; // 공연 장소 (간단한 위치)

    @Column
    private String eventAddress; // 공연 장소 (도로명)

    @NotBlank(message = "공연 일정은 필수로 입력해야 합니다.")
    @Column(nullable = false)
    private String eventDate; // 공연 일정 (날짜)

    @NotBlank(message = "공연 설명은 필수로 입력해야 합니다.")
    @Column(nullable = false, length = 2500)
    private String eventDescription; //공연 설명 (내용)

    @Column
    private String precautions; // 공연 유의 사항 및 예매 방법 안내

    @Column
    private String eventInfoLink; // 공연 정보 링크

    @Column
    private String eventPosterUrl; // 공연 포스터 및 사진 (경로 저장)

    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 관계 설정
    @JoinColumn(name = "user_id")  // user_id를 외래키로 사용
    private User postWriter; // 게시글 작성자 => User 테이블의 userName

    @CreationTimestamp
    private LocalDateTime postTime; // 게시글 작성 시간

    public void setEventPosterUrl(String eventPosterUrl) {    // eventPosterUrl 접근자 메서드 추가
        this.eventPosterUrl = eventPosterUrl;
    }

}
