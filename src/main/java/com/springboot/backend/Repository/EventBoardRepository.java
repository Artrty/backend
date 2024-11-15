package com.springboot.backend.Repository;

import com.springboot.backend.Entity.EventBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventBoardRepository extends JpaRepository<EventBoard, Long> {
    List<EventBoard> findByEventTitle(String title); // 공연 제목으로 검색
}
