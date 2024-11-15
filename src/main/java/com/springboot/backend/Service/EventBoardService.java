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
}
