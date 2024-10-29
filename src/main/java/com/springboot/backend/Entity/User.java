package com.springboot.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid; // 기본키

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @Column(nullable = false, unique = true)
    private String phoneNumber;


    @NotBlank(message = "사용자 이름은 필수 항목입니다.")
    @Column(nullable = false)
    private String userName; // 사용자 이름

    @OneToMany(mappedBy = "postWriter", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EventBoard> eventBoards;  // 한 사용자가 작성한 여러 게시글 / 해당 사용자가 삭제될 때 관련된 게시글도 함께 삭제

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Column(nullable = false)
    private String password; // 비밀번호

    @CreationTimestamp
    @Column(updatable = false) // 생성 후에는 업데이트되지 않도록 설정
    private LocalDateTime createdAt; // 계정 생성일 (자동 저장)

}