package com.springboot.backend.Controller;

import com.springboot.backend.Entity.EventBoard;
import com.springboot.backend.Repository.EventBoardRepository;
import com.springboot.backend.Response.ApiResponse;
import com.springboot.backend.Response.ErrorCode;
import com.springboot.backend.Response.SuccessCode;
import com.springboot.backend.Service.EventBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/event-board")
public class EventController {

    private final EventBoardService eventBoardService;
    private final EventBoardRepository eventBoardRepository;

    @Autowired
    public EventController(EventBoardService eventBoardService, EventBoardRepository eventBoardRepository) {
        this.eventBoardService = eventBoardService;
        this.eventBoardRepository = eventBoardRepository;
    }

    // 게시글 작성 (작성 후 관리자 검토 필요)
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new post", description = "게시글을 작성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시글 저장 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": {\"savedEventBoardId\": 1}, \"message\": \"게시글 내용 저장 성공\", \"code\": \"B002\"}"))),
    })
    public ResponseEntity<ApiResponse<?>> postEvent(
            HttpServletRequest request,
            @RequestParam(value = "image") MultipartFile image,
            @ModelAttribute EventBoard eventBoard) throws IOException {
        System.out.println("EventController 진행");
        System.out.println(image);
        System.out.println(eventBoard);

        Long savedEventBoardId = eventBoardService.keepEventBoard(image, eventBoard);

        Map<String, Object> data = new HashMap<>();
        data.put("savedEventBoardId", savedEventBoardId);

        return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.EventBoardSaveSuccess, data));
    }

    // 게시글 조회
    @GetMapping("/{id}")
    @Operation(summary = "Get an event post by ID", description = "ID를 사용하여 게시글을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시글 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": {\"id\": 1, \"title\": \"Sample Title\", \"content\": \"Sample Content\"}, \"message\": \"게시글 조회 성공\", \"code\": \"B003\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "게시글 조회 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": null, \"message\": \"게시글 조회 실패 : 해당 ID에 대한 게시물 데이터가 존재하지 않습니다.\", \"code\": \"B004\"}")))
    })
    public ResponseEntity<ApiResponse<?>> getEventById(@PathVariable Long id) {
        // ID로 EventBoard 데이터베이스 조회
        EventBoard eventBoard = eventBoardRepository.findById(id).orElse(null);
        System.out.println("게시글 ID를 통한 해당 게시글 존재 여부 확인");
        // 게시글 존재 여부 확인
        if (eventBoard != null) {
            // 게시글이 존재하는 경우 Map에 데이터를 넣어 응답
            Map<String, Object> data = new HashMap<>();
            data.put("eventBoard", eventBoard);
            System.out.println("해당 ID에 대한 게시글이 존재합니다.");
            // 성공 응답 반환
            return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.EventBoardLoadSuccess, data));
        } else {
            // 게시글이 존재하지 않는 경우 404 응답과 함께 실패 메시지 반환
            System.out.println("해당 ID에 대한 게시글이 존재하지 않습니다.");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.errorResponse(ErrorCode.EventBoardLoadException));
        }
    }

}
