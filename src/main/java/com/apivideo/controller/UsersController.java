package com.apivideo.controller;

import com.apivideo.entity.Users;
import com.apivideo.service.UsersService;
import com.apivideo.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "用户控制器", description = "管理用户相关的API")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @ApiOperation(value = "用户注册", notes = "用户通过提供用户名和密码注册新账户")
    @PostMapping("/register")
    public Map<String, String> register(@ApiParam(value = "用户信息", required = true) @RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean result = usersService.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", result ? "User registered successfully" : "User registration failed");
        return response;
    }

    @ApiOperation(value = "用户登录", notes = "用户通过提供用户名和密码登录")
    @PostMapping("/login")
    public Map<String, String> login(@ApiParam(value = "用户信息", required = true) @RequestBody Users user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        return response;
    }

    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户的信息")
    @GetMapping("/info")
    public Map<String, Object> getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            Users user = usersService.findByUsername(username);
            if (user != null) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", user.getUserId());
                userInfo.put("username", user.getUsername());
                // 如果你有更多需要返回的用户信息，可以在这里添加
                return userInfo;
            }
        }
        return null;
    }
}
