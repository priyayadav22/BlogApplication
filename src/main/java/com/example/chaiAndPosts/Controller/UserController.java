package com.example.chaiAndPosts.Controller;

import com.example.chaiAndPosts.DTO.UserRequestDto;
import com.example.chaiAndPosts.Service.UserService;
import com.example.chaiAndPosts.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto user){
        try{
            User savedUser = userService.registerUser(user.getUsername(), user.getPassword());
            return  ResponseEntity.ok("User Registered : " + savedUser.getUsername());
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto user) {
        userService.authenticate(user.getUsername(), user.getPassword());
        return ResponseEntity.ok("Login successful");
    }

}
