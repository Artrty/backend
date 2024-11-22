package com.springboot.backend.Repository;

import com.springboot.backend.Entity.EventBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventBoardRepository extends JpaRepository<EventBoard, Long> {

    Optional<EventBoard> findById(Long id);
    List<EventBoard> findByEventTitle(String title); // 공연 제목으로 검색

    // ID로 관리자 승인된 게시글 조회
    EventBoard findByIdAndApprovalStatusTrue(Long id);

    // 관리자 승인된 모든 게시글 조회
    List<EventBoard> findAllByApprovalStatusTrue();
}
