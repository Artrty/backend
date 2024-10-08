package com.springboot.backend.Controller;

import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 전화번호로 유저 존재 여부 확인
    @GetMapping("/{phoneNumber}/valid")
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> isUserExists(@PathVariable String phoneNumber) {
        System.out.println("전화번호를 통한 사용자 존재 확인");
        // User가 존재하는지 확인
        User user = userRepository.findByPhoneNumber(phoneNumber);
        boolean exists = user != null; // 유저가 존재하면 true, 없으면 false 반환

        return ResponseEntity.ok(ApiResponse.successResponse(exists, exists ? "사용자의 정보가 존재합니다." : "사용자의 정보가 존재하지 않습니다."));
    }
}
