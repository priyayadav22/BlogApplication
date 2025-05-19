package com.example.chaiAndPosts.Service;
import com.example.chaiAndPosts.Exception.InvalidCredentialsException;
import com.example.chaiAndPosts.Repository.UserRepository;
import com.example.chaiAndPosts.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password){
        if(userRepository.findByUsername(username).isPresent()){
            throw  new RuntimeException("username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean authenticate(String username, String rawPassword){
        Optional<User> useropt = userRepository.findByUsername(username);
        if(useropt.isEmpty() || !passwordEncoder.matches(rawPassword, useropt.get().getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return true;
    }

}
