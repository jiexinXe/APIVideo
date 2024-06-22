package com.apivideo.grpc;

import com.apivideo.entity.Likes;
import com.apivideo.grpc.LikesProto.LikeCountResponse;
import com.apivideo.grpc.LikesProto.LikeRequest;
import com.apivideo.grpc.LikesProto.LikeResponse;
import com.apivideo.service.LikesService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl extends LikeServiceGrpc.LikeServiceImplBase {

    @Autowired
    private LikesService likesService;

    @Override
    public void addLike(LikeRequest request, StreamObserver<LikeResponse> responseObserver) {
        Likes like = new Likes();
        like.setVideoId(request.getVideoId());
        like.setUserId(request.getUserId());

        likesService.save(like);

        LikeResponse response = LikeResponse.newBuilder()
                .setStatus(0)
                .setMessage("Like added successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getLike(LikeRequest request, StreamObserver<LikeRequest> responseObserver) {
        Likes like = likesService.getById(request.getLikeId());
        if (like != null) {
            LikeRequest response = LikeRequest.newBuilder()
                    .setLikeId(like.getLikeId())
                    .setVideoId(like.getVideoId())
                    .setUserId(like.getUserId())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Like not found"));
        }
    }

    @Override
    public void deleteLike(LikeRequest request, StreamObserver<LikeResponse> responseObserver) {
        boolean deleted = likesService.removeById(request.getLikeId());
        LikeResponse response = LikeResponse.newBuilder()
                .setStatus(deleted ? 0 : 1)
                .setMessage(deleted ? "Like deleted successfully" : "Like deletion failed")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateLike(LikeRequest request, StreamObserver<LikeResponse> responseObserver) {
        Likes like = likesService.getById(request.getLikeId());
        if (like != null) {
            // Update logic if needed

            LikeResponse response = LikeResponse.newBuilder()
                    .setStatus(0)
                    .setMessage("Like updated successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Like not found"));
        }
    }

    @Override
    public void countByUserIdAndVideoId(LikeRequest request, StreamObserver<LikeCountResponse> responseObserver) {
        // 手动实现 countByUserIdAndVideoId 方法逻辑
        int count = Math.toIntExact(likesService.lambdaQuery()
                .eq(Likes::getUserId, request.getUserId())
                .eq(Likes::getVideoId, request.getVideoId())
                .count());

        LikeCountResponse response = LikeCountResponse.newBuilder()
                .setCount(count)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
