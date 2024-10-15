package com.springboot.backend.Controller;

import com.springboot.backend.Repository.EventRepository;
import com.springboot.backend.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event-board")
public class EventController {

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> postEvent(@RequestBody EventRepository eventRepository) {
        return null;
    }
}
