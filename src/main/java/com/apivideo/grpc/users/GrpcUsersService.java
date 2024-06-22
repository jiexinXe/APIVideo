package com.apivideo.grpc.users;

import com.apivideo.entity.Users;
import com.apivideo.grpc.common.UserRequest;
import com.apivideo.service.UsersService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrpcUsersService extends UsersServiceGrpc.UsersServiceImplBase {

    @Autowired
    private UsersService usersService;

    @Override
    public void getUser(UserRequest request, StreamObserver<UserReply> responseObserver) {
        Users user = usersService.getById(request.getUserId());
        if (user != null) {
            UserReply reply = UserReply.newBuilder()
                    .setId(user.getUserId())
                    .setName(user.getUsername())
                    .setEmail(user.getEmail())
                    .setUsername(user.getUsername())
                    .build();
            responseObserver.onNext(reply);
        } else {
            responseObserver.onError(new Exception("User not found"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void findByUsername(UsernameRequest request, StreamObserver<UserReply> responseObserver) {
        Users user = usersService.findByUsername(request.getUsername());
        if (user != null) {
            UserReply reply = UserReply.newBuilder()
                    .setId(user.getUserId())
                    .setName(user.getUsername())
                    .setEmail(user.getEmail())
                    .setUsername(user.getUsername())
                    .build();
            responseObserver.onNext(reply);
        } else {
            responseObserver.onError(new Exception("User not found"));
        }
        responseObserver.onCompleted();
    }
}
