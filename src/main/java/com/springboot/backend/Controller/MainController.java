package com.springboot.backend.Controller;

import com.springboot.backend.Entity.User;
import com.springboot.backend.Repository.UserRepository;
import com.springboot.backend.Service.CoolSmsService;
import com.springboot.backend.Service.SmsCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    @ResponseBody
    public String main() {
        return "main";
    }

    private final RestTemplate restTemplate = new RestTemplate();
}
