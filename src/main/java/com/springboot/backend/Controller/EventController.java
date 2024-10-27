package com.springboot.backend.Controller;

import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/event-board")
public class EventController {

    // 게시글 작성 (작성 후 관리자 검토 필요)
    @ResponseBody
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> postEvent(
            HttpServletRequest request,
            @RequestParam(value = "image") MultipartFile image,
            @ModelAttribute EventBoard eventBoard) throws IOException {
        System.out.println("게시글 작성 진행");
        System.out.println(image);
        System.out.println(eventBoard);
        return null;
    }
}
