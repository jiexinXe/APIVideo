package com.apivideo.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class GrpcServer {

    private Server server;

    @Autowired
    private ViewServiceImpl viewsService;

    @PostConstruct
    public void start() {
        try {
            server = ServerBuilder.forPort(9090)
                    .addService(viewsService)
                    .build()
                    .start();
            System.out.println("Server started, listening on " + 9090);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down gRPC server since JVM is shutting down");
                this.stop();
                System.out.println("Server shut down");
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
}
