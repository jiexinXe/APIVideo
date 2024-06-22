package com.apivideo.client;

import com.apivideo.grpc.common.UserRequest;
import com.apivideo.grpc.users.UserReply;
import com.apivideo.grpc.users.UsernameRequest;
import com.apivideo.grpc.users.UsersServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class UsersClient {

    private final UsersServiceGrpc.UsersServiceBlockingStub blockingStub;

    public UsersClient(ManagedChannel channel) {
        blockingStub = UsersServiceGrpc.newBlockingStub(channel);
    }

    public void getUser(int userId) {
        UserRequest request = UserRequest.newBuilder().setUserId(userId).build();
        try {
            UserReply response = blockingStub.getUser(request);
            System.out.println("User ID: " + response.getId());
            System.out.println("User Name: " + response.getName());
            System.out.println("User Email: " + response.getEmail());
            System.out.println("Username: " + response.getUsername());
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: " + e.getStatus());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    public void findByUsername(String username) {
        UsernameRequest request = UsernameRequest.newBuilder().setUsername(username).build();
        try {
            UserReply response = blockingStub.findByUsername(request);
            System.out.println("User ID: " + response.getId());
            System.out.println("User Name: " + response.getName());
            System.out.println("User Email: " + response.getEmail());
            System.out.println("Username: " + response.getUsername());
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: " + e.getStatus());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String target = "localhost:9090";  // gRPC服务器地址和端口
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();

        UsersClient client = new UsersClient(channel);

        try {
            System.out.println("Requesting user with ID 1:");
            client.getUser(1);

            System.out.println("Requesting user with username 'gxc':");
            client.findByUsername("gxc");
        } finally {
            channel.shutdownNow();
        }
    }
}
