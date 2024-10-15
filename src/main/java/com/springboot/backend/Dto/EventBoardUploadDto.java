package com.springboot.backend.Dto;

import com.springboot.backend.Entity.EventBoard;
import jdk.jfr.Event;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EventBoardUploadDto {

    private MultipartFile file;

    public EventBoard toEntity(String eventPosterUrl) {
        return EventBoard.builder()
                .eventPosterUrl(eventPosterUrl)
                .build();
    }
}
