package com.apivideo.grpc;

import com.apivideo.entity.Users;
import com.apivideo.grpc.UsersProto.UserRequest;
import com.apivideo.grpc.UsersProto.UserResponse;
import com.apivideo.grpc.UsersProto.UserLoginResponse;
import com.apivideo.service.UsersService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        usersService.save(user);

        UserResponse response = UserResponse.newBuilder()
                .setUserId(user.getUserId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setMessage("User registered successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void loginUser(UserRequest request, StreamObserver<UserLoginResponse> responseObserver) {
        Users user = usersService.findByUsername(request.getUsername());
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = "generated_jwt_token"; // 使用实际的 JWT 生成逻辑
            UserLoginResponse response = UserLoginResponse.newBuilder()
                    .setToken(token)
                    .setMessage("Login successful")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Invalid username or password"));
        }
    }

    @Override
    public void getUserInfo(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        Users user = usersService.getById(request.getUserId());
        if (user != null) {
            UserResponse response = UserResponse.newBuilder()
                    .setUserId(user.getUserId())
                    .setUsername(user.getUsername())
                    .setEmail(user.getEmail())
                    .setMessage("User info retrieved successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("User not found"));
        }
    }
}
