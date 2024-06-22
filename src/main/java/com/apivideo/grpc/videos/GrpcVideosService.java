package com.apivideo.grpc.videos;

import com.apivideo.entity.Videos;
import com.apivideo.service.VideosService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrpcVideosService extends VideosServiceGrpc.VideosServiceImplBase {

    @Autowired
    private VideosService videosService;

    @Override
    public void getVideo(VideoRequest request, StreamObserver<VideoReply> responseObserver) {
        Videos video = videosService.getById(request.getId());
        VideoReply reply = VideoReply.newBuilder()
                .setId(video.getVideoId())
                .setTitle(video.getTitle())
                .setUrl(video.getVideoPath())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void getRecommendedVideos(RecommendedVideosRequest request, StreamObserver<VideosReply> responseObserver) {
        List<Videos> videos = videosService.getRecommendedVideos(request.getUserId(), request.getLimit());
        VideosReply.Builder builder = VideosReply.newBuilder();
        for (Videos video : videos) {
            builder.addVideos(VideoReply.newBuilder()
                    .setId(video.getVideoId())
                    .setTitle(video.getTitle())
                    .setUrl(video.getVideoPath())
                    .build());
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getRandomVideos(RandomVideosRequest request, StreamObserver<VideosReply> responseObserver) {
        List<Videos> videos = videosService.getRandomVideos(request.getLimit());
        VideosReply.Builder builder = VideosReply.newBuilder();
        for (Videos video : videos) {
            builder.addVideos(VideoReply.newBuilder()
                    .setId(video.getVideoId())
                    .setTitle(video.getTitle())
                    .setUrl(video.getVideoPath())
                    .build());
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
