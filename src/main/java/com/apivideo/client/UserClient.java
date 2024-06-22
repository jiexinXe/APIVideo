package com.apivideo.client;

import com.apivideo.entity.Users;
import com.apivideo.grpc.UserServiceGrpc;
import com.apivideo.grpc.UsersProto.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserClient {

    @GrpcClient("grpc-server")
    UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public boolean registerUser(Users user) {
        UserRequest request = UserRequest.newBuilder()
                .setUserId(user.getUserId())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .build();
        UserResponse response = this.blockingStub.registerUser(request);
        return true;
    }

    public String loginUser(Users user) {
        UserRequest request = UserRequest.newBuilder()
                .setUserId(user.getUserId())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .build();
        UserLoginResponse response = this.blockingStub.loginUser(request);
        return response.getToken();
    }

    public Users getUserInfo(String username) {
        UserRequest request = UserRequest.newBuilder()
                .setUsername(username)
                .build();
        UserResponse response = this.blockingStub.getUserInfo(request);
        Users user = new Users();
        user.setEmail(response.getEmail())
                .setUsername(response.getUsername())
                .setUserId(response.getUserId())
                .setEmail(response.getEmail());

        return user;
    }
}
