package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.dto.UserStatusResponse;
import com.zmn.PinBotChat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/status")
    public ResponseEntity<UserStatusResponse> getUserStatus(@PathVariable Long userId) {
        UserStatusResponse statusResponse = userService.getUserStatusById(userId);
        if (statusResponse != null) {
            return ResponseEntity.ok(statusResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
