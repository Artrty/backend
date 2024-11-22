package com.springboot.backend.Service;

import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Repository.EventBoardRepository;
import com.springboot.backend.Response.ApiResponse;
import com.springboot.backend.Response.SuccessCode;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EventBoardService {
    private final EventBoardRepository eventBoardRepository;

    @Autowired
    private S3Uploader s3Uploader;

    public EventBoardService(EventBoardRepository eventBoardRepository) {
        this.eventBoardRepository = eventBoardRepository;
    }

    @Transactional
    public Long keepEventBoard(MultipartFile image, EventBoard eventBoard) throws IOException {
        System.out.println("EventBoardService 실행");
        
        if (image != null && !image.isEmpty()) { // 이미지가 null혹은 비어있지 않을 경우
            String storedFileName = s3Uploader.upload(image, "images");
            eventBoard.setEventPosterUrl(storedFileName);
        }
        EventBoard saveEventBoard = eventBoardRepository.save(eventBoard);
        return saveEventBoard.getId();
    }

    @Transactional(readOnly = true)
    public EventBoard getApprovedEventById(Long id) { // 특정 id에 따른 게시글 조회 (관리자 승인)
        return eventBoardRepository.findByIdAndApprovalStatusTrue(id);
    }

    @Transactional(readOnly = true)
    public List<EventBoard> getAllApprovedEvents() { // 전체 게시글 조회 (관리자 승인)
        return eventBoardRepository.findAllByApprovalStatusTrue();
    }

    // 관리자의 게시글 승인 처리 로직
    @Transactional
    public EventBoard approveEvent(Long id) {
        // ID에 해당하는 게시글 찾기
        Optional<EventBoard> optionalEventBoard = eventBoardRepository.findById(id);

        // 게시글이 존재하지 않을 경우 예외 처리
        EventBoard eventBoard = optionalEventBoard.orElseThrow();

        // 게시물에 대한 관리자 승인 상태 변경
        eventBoard.setApprovalStatus(true);

        // 변경된 엔터티 저장 후 반환
        return eventBoardRepository.save(eventBoard);
    }


}
