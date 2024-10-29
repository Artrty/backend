package com.springboot.backend.Controller;

import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Response.ApiResponse;
import com.springboot.backend.Service.EventBoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/event-board")
public class EventController {

    @Autowired
    private EventBoardService eventBoardService;

    // 게시글 작성 (작성 후 관리자 검토 필요)
    @ResponseBody
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> postEvent(
            HttpServletRequest request,
            @RequestParam(value = "image") MultipartFile image,
            @ModelAttribute EventBoard eventBoard) throws IOException {
        System.out.println("EventController 진행");
        System.out.println(image);
        System.out.println(eventBoard);

        Long savedEventBoardId = eventBoardService.keepEventBoard(image, eventBoard);

        return null;
    }
}
