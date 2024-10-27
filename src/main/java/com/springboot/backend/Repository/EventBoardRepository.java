package com.springboot.backend.Repository;

import com.springboot.backend.Entity.EventBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventBoardRepository extends JpaRepository<EventBoard, Long> {
}
