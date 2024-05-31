package com.apivideo.controller;

import com.apivideo.entity.Users;
import com.apivideo.service.UsersService;
import com.apivideo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean result = usersService.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", result ? "User registered successfully" : "User registration failed");
        return response;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Users user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        return response;
    }

    @GetMapping("/info")
    public Users getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return usersService.findByUsername(username);
        }
        return null;
    }
}
