package com.springboot.backend.Controller;

import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 전화번호로 유저 존재 여부 확인
    @GetMapping("/{phoneNumber}/valid")
    @ResponseBody
    public boolean isUserExists(@PathVariable String phoneNumber) {
        // User가 존재하는지 확인
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return user != null; // 유저가 존재하면 true, 없으면 false 반환
    }
}
