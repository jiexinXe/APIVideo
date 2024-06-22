package com.apivideo.grpc;

import com.apivideo.grpc.ViewsProto.ViewRequest;
import com.apivideo.grpc.ViewsProto.ViewStatusResponse;
import com.apivideo.grpc.ViewsProto.ViewListResponse;
import com.apivideo.service.ViewsService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewServiceImpl extends ViewServiceGrpc.ViewServiceImplBase {

    @Autowired
    private ViewsService viewsService;

    @Override
    public void addView(ViewRequest request, StreamObserver<ViewStatusResponse> responseObserver) {
        viewsService.addViewedVideo(request.getUserId(), request.getVideoId());

        ViewStatusResponse response = ViewStatusResponse.newBuilder()
                .setMessage("View added successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getViewedVideoIds(ViewRequest request, StreamObserver<ViewListResponse> responseObserver) {
        List<Integer> videoIds = viewsService.getViewedVideoIds(request.getUserId());

        ViewListResponse response = ViewListResponse.newBuilder()
                .addAllVideoIds(videoIds)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteViewsByVideoId(ViewRequest request, StreamObserver<ViewStatusResponse> responseObserver) {
        viewsService.deleteViewsByVideoId(request.getVideoId());

        ViewStatusResponse response = ViewStatusResponse.newBuilder()
                .setMessage("Views deleted successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void clearViewedVideos(ViewRequest request, StreamObserver<ViewStatusResponse> responseObserver) {
        viewsService.clearViewedVideos(request.getUserId());

        ViewStatusResponse response = ViewStatusResponse.newBuilder()
                .setMessage("Viewed videos cleared successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
