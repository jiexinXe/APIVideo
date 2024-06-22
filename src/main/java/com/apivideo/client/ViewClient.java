package com.apivideo.client;

import com.apivideo.grpc.ViewServiceGrpc;
import com.apivideo.grpc.ViewsProto.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewClient {

    @GrpcClient("grpc-server")
    private ViewServiceGrpc.ViewServiceBlockingStub blockingStub;

    public void addView(int userid, int videoid) {
        ViewRequest request = ViewRequest.newBuilder()
                .setUserId(userid)
                .setVideoId(videoid)
                .build();
        ViewStatusResponse response = this.blockingStub.addView(request);
    }

    public List<Integer> getViewedVideoIds(int userid) {
        ViewRequest request = ViewRequest.newBuilder()
                .setUserId(userid)
                .build();
        ViewListResponse response = this.blockingStub.getViewedVideoIds(request);
        return response.getVideoIdsList();
    }

    public String deleteViewsByVideoId(int videoid) {
        ViewRequest request = ViewRequest.newBuilder()
                .setVideoId(videoid)
                .build();
        ViewStatusResponse response = this.blockingStub.deleteViewsByVideoId(request);
        return response.getMessage();
    }

    public String clearViewedVideos(int userid) {
        ViewRequest request = ViewRequest.newBuilder()
                .setUserId(userid)
                .build();
        ViewStatusResponse response = this.blockingStub.clearViewedVideos(request);
        return response.getMessage();
    }
}
