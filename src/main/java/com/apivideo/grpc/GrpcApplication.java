package com.apivideo.grpc;

import com.apivideo.grpc.comments.GrpcCommentsService;
import com.apivideo.grpc.likes.GrpcLikesService;
import com.apivideo.grpc.users.GrpcUsersService;
import com.apivideo.grpc.videos.GrpcVideosService;
import com.apivideo.grpc.views.GrpcViewsService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.apivideo.service", "com.apivideo.grpc"})
@MapperScan("com.apivideo.mapper")
public class GrpcApplication implements CommandLineRunner {

    @Autowired
    private GrpcCommentsService grpcCommentsService;
    @Autowired
    private GrpcLikesService grpcLikesService;
    @Autowired
    private GrpcUsersService grpcUsersService;
    @Autowired
    private GrpcVideosService grpcVideosService;
    @Autowired
    private GrpcViewsService grpcViewsService;

    public static void main(String[] args) {
        SpringApplication.run(GrpcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Server server = ServerBuilder.forPort(9090)
                .addService(grpcCommentsService)
                .addService(grpcLikesService)
                .addService(grpcUsersService)
                .addService(grpcVideosService)
                .addService(grpcViewsService)
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            if (server != null) {
                server.shutdown();
            }
            System.err.println("*** server shut down");
        }));

        server.awaitTermination();
    }
}
