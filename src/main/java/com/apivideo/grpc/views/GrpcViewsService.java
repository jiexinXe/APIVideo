package com.apivideo.grpc.views;

import com.apivideo.entity.Views;
import com.apivideo.grpc.common.UserRequest;
import com.apivideo.service.ViewsService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrpcViewsService extends ViewsServiceGrpc.ViewsServiceImplBase {

    @Autowired
    private ViewsService viewsService;

    @Override
    public void addViewedVideo(ViewedVideoRequest request, StreamObserver<ViewReply> responseObserver) {
        viewsService.addViewedVideo(request.getUserId(), request.getVideoId());
        // 模拟 getLastViewedVideo 方法
        List<Integer> viewedVideoIds = viewsService.getViewedVideoIds(request.getUserId());
        Integer lastVideoId = viewedVideoIds.isEmpty() ? null : viewedVideoIds.get(viewedVideoIds.size() - 1);
        if (lastVideoId != null) {
            Views view = new Views();
            view.setViewId(lastVideoId);
            view.setUserId(request.getUserId());
            view.setVideoId(request.getVideoId());
            ViewReply reply = ViewReply.newBuilder()
                    .setId(view.getViewId())
                    .setUserId(view.getUserId())
                    .setVideoId(view.getVideoId())
                    .build();
            responseObserver.onNext(reply);
        } else {
            responseObserver.onError(new Exception("Viewed video not found"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getViewedVideos(UserRequest request, StreamObserver<ViewedVideosReply> responseObserver) {
        List<Integer> viewedVideoIds = viewsService.getViewedVideoIds(request.getUserId());
        ViewedVideosReply.Builder builder = ViewedVideosReply.newBuilder();
        for (Integer videoId : viewedVideoIds) {
            Views view = new Views();
            view.setViewId(videoId);
            view.setUserId(request.getUserId());
            // 模拟获取其他字段
            builder.addViews(ViewReply.newBuilder()
                    .setId(view.getViewId())
                    .setUserId(view.getUserId())
                    .setVideoId(view.getVideoId())
                    .build());
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
