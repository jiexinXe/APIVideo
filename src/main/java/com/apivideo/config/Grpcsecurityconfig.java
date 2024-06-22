package com.apivideo.config;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import net.devh. boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.annotation.Nullable;

@Configuration
public class Grpcsecurityconfig {
@Bean
public GrpcAuthenticationReader grpcAuthenticationReader(){//返回一个默认的
    // GrpcAuthenticationReader 实例
     return new GrpcAuthenticationReader() {
         @Nullable
         @Override
         public Authentication readAuthentication(ServerCall<?, ?> serverCall, Metadata metadata) throws AuthenticationException {
             return null;
         }
     };
     }
}