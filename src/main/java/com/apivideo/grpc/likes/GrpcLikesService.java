package com.apivideo.grpc.likes;

import com.apivideo.entity.Likes;
import com.apivideo.service.LikesService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrpcLikesService extends LikesServiceGrpc.LikesServiceImplBase {

    @Autowired
    private LikesService likesService;

    @Override
    public void getLike(LikeRequest request, StreamObserver<LikeReply> responseObserver) {
        Likes like = likesService.getById(request.getId());
        LikeReply reply = LikeReply.newBuilder()
                .setId(like.getLikeId())
                .setUserId(like.getUserId())
                .setVideoId(like.getVideoId())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
