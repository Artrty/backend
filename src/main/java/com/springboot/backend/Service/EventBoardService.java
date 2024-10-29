package com.springboot.backend.Service;

import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Repository.EventBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EventBoardService {
    private final EventBoardRepository eventBoardRepository; // 이름을 소문자로 수정

    @Autowired
    private S3Uploader s3Uploader;

    public EventBoardService(EventBoardRepository eventBoardRepository) {
        this.eventBoardRepository = eventBoardRepository; // 이름을 소문자로 수정
    }

    @Transactional
    public Long keepEventBoard(MultipartFile image, EventBoard eventBoard) throws IOException {
        System.out.println("EventBoardService 실행");
        if(!image.isEmpty()) {
            String storedFileName = s3Uploader.upload(image, "images");
            eventBoard.setEventPosterUrl(storedFileName); // 수정된 메서드 사용
        }
        EventBoard saveEventBoard = eventBoardRepository.save(eventBoard);
        return saveEventBoard.getId();
    }
}
