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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
            @RequestParam(value = "image", required = false) MultipartFile image,
            @ModelAttribute EventBoard eventBoard) throws IOException {
        System.out.println("EventController 진행");
        System.out.println(image);
        System.out.println(eventBoard);

        Long savedEventBoardId = eventBoardService.keepEventBoard(image, eventBoard);

        Map<String, Object> data = new HashMap<>();
        data.put("savedEventBoardId", savedEventBoardId);

        return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.EventBoardSaveSuccess, data));
    }

    // 특정 게시글 상세 조회
    @GetMapping("/{id}")
    @Operation(summary = "Get an event post by ID", description = "ID를 사용하여 특정 게시글을 조회합니다.")
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

    // 전체 게시글 조회
    @GetMapping(value = "/viewAll")
    @Operation(summary = "Show all events", description = "모든 게시글을 조회합니다 조회합니다.")
    public ResponseEntity<ApiResponse<?>> viewAll() {
        List<EventBoard> eventBoard = eventBoardRepository.findAll();
        Map<String, Object> data = new HashMap<>();
        data.put("eventBoard", eventBoard);
        return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.EventBoardAllLoadSuccess, data));
    };
    
    // 게시글 수정
    @PutMapping(value = "/{id}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Edit event post", description = "게시글을 수정합니다.")
    public ResponseEntity<ApiResponse<?>> editEventById(
            @PathVariable Long id,
            @RequestParam(required = false) String eventTitle,
            @RequestParam(required = false) String eventLocation,
            @RequestParam(required = false) String eventAddress,
            @RequestParam(required = false) String eventDate,
            @RequestParam(required = false) String eventDescription,
            @RequestParam(required = false) String precautions,
            @RequestParam(required = false) String eventInfoLink,
            @RequestParam(required = false) String eventPosterUrl) {

        // ID로 기존 게시글 조회
        EventBoard existingEventBoard = eventBoardRepository.findById(id).orElse(null);

        if (existingEventBoard != null) {
            // 전달된 값이 null이 아닌 경우에만 필드 업데이트
            if (eventTitle != null) existingEventBoard.setEventTitle(eventTitle);
            if (eventLocation != null) existingEventBoard.setEventLocation(eventLocation);
            if (eventAddress != null) existingEventBoard.setEventAddress(eventAddress);
            if (eventDate != null) existingEventBoard.setEventDate(eventDate);
            if (eventDescription != null) existingEventBoard.setEventDescription(eventDescription);
            if (precautions != null) existingEventBoard.setPrecautions(precautions);
            if (eventInfoLink != null) existingEventBoard.setEventInfoLink(eventInfoLink);
            if (eventPosterUrl != null) existingEventBoard.setEventPosterUrl(eventPosterUrl);

            // 저장 후 응답 데이터 구성
            EventBoard savedEventBoard = eventBoardRepository.save(existingEventBoard);
            Map<String, Object> data = new HashMap<>();
            data.put("eventBoard", savedEventBoard);

            // 성공 응답 반환
            return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.EventBoardUpdateSuccess, data));
        } else {
            return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.EventBoardUpdateFailed, null));
        }
    }

    // 게시글 삭제
    @DeleteMapping(value = "/{id}/delete")
    @Operation(summary = "Delete event post", description = "게시글을 삭제합니다")
    public ResponseEntity<ApiResponse<?>> deleteEventById(@PathVariable Long id) {
        EventBoard eventBoard = eventBoardRepository.findById(id).orElse(null);
        if (eventBoard != null) {
            eventBoardRepository.delete(eventBoard);
            return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.EventBoardDeleteSuccess, null));
        } else {
            return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.EventBoardDeleteFailed, null));
        }
    };

}
