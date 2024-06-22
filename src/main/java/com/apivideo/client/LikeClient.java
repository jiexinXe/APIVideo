package com.apivideo.client;

import com.apivideo.entity.Likes;
import com.apivideo.grpc.LikeServiceGrpc;
import com.apivideo.grpc.LikesProto;
import com.apivideo.grpc.LikesProto.LikeRequest;
import com.apivideo.grpc.LikesProto.LikeResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class LikeClient {
    @GrpcClient("grpc-server")
    private LikeServiceGrpc.LikeServiceBlockingStub blockingStub;

    public void addLike(Likes like) {
        LikeRequest request = LikeRequest.newBuilder().setLikeId(like.getLikeId())
                .setUserId(like.getUserId())
                .setVideoId(like.getVideoId())
                .build();
        LikeResponse response = this.blockingStub.addLike(request);
    }

    public Likes getLike(int id) {
        LikeRequest request = LikeRequest.newBuilder().setLikeId(id)
                .build();
        LikeRequest response = this.blockingStub.getLike(request);
        Likes like = new Likes();
        like.setUserId(response.getUserId());
        like.setVideoId(response.getVideoId());
        like.setLikeId(response.getLikeId());
        return like;
    }

    public int deleteLike(int id) {
        LikeRequest request = LikeRequest.newBuilder().setLikeId(id)
                .build();
        LikeResponse response = this.blockingStub.deleteLike(request);
        return response.getStatus();
    }

    public int updateLike(Likes like) {
        LikeRequest request = LikeRequest.newBuilder().setLikeId(like.getLikeId())
                .setVideoId(like.getVideoId())
                .setUserId(like.getUserId())
                .build();
        LikeResponse response = this.blockingStub.updateLike(request);
        return response.getStatus();
    }

    public int countByUserIdAndVideoId(int userid, int videoid) {
        LikeRequest request = LikeRequest.newBuilder()
                .setVideoId(videoid)
                .setUserId(userid)
                .build();
        LikesProto.LikeCountResponse response = this.blockingStub.countByUserIdAndVideoId(request);
        return response.getCount();
    }
}
