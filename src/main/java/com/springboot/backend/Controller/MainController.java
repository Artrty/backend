package com.springboot.backend.Controller;

import com.springboot.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    @ResponseBody
    public String main() {
        return "test!";
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    @ResponseBody
    public String signup() {
        Long userId;

        return "test!";
    }
}


