package com.springboot.backend.Controller;

import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Response.ApiResponse;
import com.springboot.backend.Response.ErrorCode;
import com.springboot.backend.Response.SuccessCode;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 전화번호로 유저 존재 여부 확인
    @GetMapping("/{phoneNumber}/valid")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 정보 존재",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": {\"exists\": true}, \"message\": \"사용자 정보가 존재합니다.\", \"code\": \"U003\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = "{\"data\": null, \"message\": \"사용자 정보가 존재하지 않습니다.\", \"code\": \"U504\"}")))
    })
    public ResponseEntity<ApiResponse<?>> isUserExists(@PathVariable String phoneNumber) {
        System.out.println("전화번호를 통한 사용자 존재 확인");

        // User가 존재하는지 확인
        User user = userRepository.findByPhoneNumber(phoneNumber);
        boolean exists = user != null; // 유저가 존재하면 true, 없으면 false 반환

        // 응답 데이터
        Map<String, Object> data = new HashMap<>();
        data.put("exists", exists);

        if (exists) {
            return ResponseEntity.ok(ApiResponse.successResponse(SuccessCode.ValidPhonenumSuccess, data));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.errorResponse(ErrorCode.InvalidPhonenumException));
        }
    }
}
