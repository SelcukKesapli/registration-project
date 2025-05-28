package com.example.registration.controller;

import com.example.registration.dto.UserDto;
import com.example.registration.dto.VerificationRequest;
import com.example.registration.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // Controller olduğunu belirtir, JSON döner
@RequestMapping("/api")  // Bu controller altındaki tüm URL'ler /api ile başlar
public class UserController {

    private final UserService userService;

    @Autowired  // Spring buraya UserService'i otomatik bağlar
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            // Hataları ekrana bastıralım
            result.getAllErrors().forEach(error -> System.out.println(">> " + error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        String response = userService.registerUser(userDto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerificationRequest request) {
        String result = userService.verifyUser(request.getEmail(), request.getCode());
        return ResponseEntity.ok(result);
    }


}
