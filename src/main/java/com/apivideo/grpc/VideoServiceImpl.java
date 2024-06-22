package com.apivideo.grpc;

import com.apivideo.entity.Users;
import com.apivideo.entity.Videos;
import com.apivideo.grpc.VideosProto.VideoRequest;
import com.apivideo.grpc.VideosProto.VideoResponse;
import com.apivideo.grpc.VideosProto.VideoListResponse;
import com.apivideo.grpc.VideosProto.VideoStatusResponse;
import com.apivideo.service.UsersService;
import com.apivideo.service.VideosService;
import com.apivideo.service.ViewsService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VideoServiceImpl extends VideoServiceGrpc.VideoServiceImplBase {

    @Autowired
    private VideosService videosService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ViewsService viewsService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void addVideo(VideoRequest request, StreamObserver<VideoStatusResponse> responseObserver) {
        Videos video = new Videos();
        video.setUserId(request.getUserId());
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setCoverPath(request.getCoverPath());
        video.setVideoPath(request.getVideoPath());
        video.setLikes(request.getLikes());
        video.setComments(request.getComments());
        video.setCollections(request.getCollections());
        video.setShares(request.getShares());
        video.setUploadTime(LocalDateTime.parse(request.getUploadTime(), formatter));

        videosService.save(video);

        VideoStatusResponse response = VideoStatusResponse.newBuilder()
                .setMessage("Video added successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getVideoById(VideoRequest request, StreamObserver<VideoResponse> responseObserver) {
        Videos video = videosService.getById(request.getVideoId());
        if (video != null) {
            // 记录用户观看历史
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                Users user = usersService.findByUsername(username);
                if (user != null) {
                    viewsService.addViewedVideo(user.getUserId(), request.getVideoId());
                }
            }

            VideoResponse response = VideoResponse.newBuilder()
                    .setVideoId(video.getVideoId())
                    .setUserId(video.getUserId())
                    .setTitle(video.getTitle())
                    .setDescription(video.getDescription())
                    .setCoverPath(video.getCoverPath())
                    .setVideoPath(video.getVideoPath())
                    .setLikes(video.getLikes())
                    .setComments(video.getComments())
                    .setCollections(video.getCollections())
                    .setShares(video.getShares())
                    .setUploadTime(video.getUploadTime().format(formatter))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Video not found"));
        }
    }

    @Override
    public void deleteVideo(VideoRequest request, StreamObserver<VideoStatusResponse> responseObserver) {
        boolean deleted = videosService.removeById(request.getVideoId());
        VideoStatusResponse response = VideoStatusResponse.newBuilder()
                .setMessage(deleted ? "Video deleted successfully" : "Video deletion failed")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateVideo(VideoRequest request, StreamObserver<VideoStatusResponse> responseObserver) {
        Videos video = videosService.getById(request.getVideoId());
        if (video != null) {
            video.setTitle(request.getTitle());
            video.setDescription(request.getDescription());
            video.setCoverPath(request.getCoverPath());
            video.setVideoPath(request.getVideoPath());
            video.setLikes(request.getLikes());
            video.setComments(request.getComments());
            video.setCollections(request.getCollections());
            video.setShares(request.getShares());
            video.setUploadTime(LocalDateTime.parse(request.getUploadTime(), formatter));
            videosService.updateById(video);

            VideoStatusResponse response = VideoStatusResponse.newBuilder()
                    .setMessage("Video updated successfully")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Exception("Video not found"));
        }
    }

    @Override
    public void getRecommendedVideos(VideoRequest request, StreamObserver<VideoListResponse> responseObserver) {
        List<Videos> videos = videosService.getRecommendedVideos(request.getUserId(), 4);
        VideoListResponse.Builder responseBuilder = VideoListResponse.newBuilder();
        for (Videos video : videos) {
            VideoResponse videoResponse = VideoResponse.newBuilder()
                    .setVideoId(video.getVideoId())
                    .setUserId(video.getUserId())
                    .setTitle(video.getTitle())
                    .setDescription(video.getDescription())
                    .setCoverPath(video.getCoverPath())
                    .setVideoPath(video.getVideoPath())
                    .setLikes(video.getLikes())
                    .setComments(video.getComments())
                    .setCollections(video.getCollections())
                    .setShares(video.getShares())
                    .setUploadTime(video.getUploadTime().format(formatter))
                    .build();
            responseBuilder.addVideos(videoResponse);
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void likeVideo(VideoRequest request, StreamObserver<VideoStatusResponse> responseObserver) {
        if (videosService.hasLiked(request.getUserId(), request.getVideoId())) {
            videosService.unlikeVideo(request.getUserId(), request.getVideoId());
            VideoStatusResponse response = VideoStatusResponse.newBuilder()
                    .setMessage("Video unliked successfully!")
                    .build();
            responseObserver.onNext(response);
        } else {
            videosService.likeVideo(request.getUserId(), request.getVideoId());
            VideoStatusResponse response = VideoStatusResponse.newBuilder()
                    .setMessage("Video liked successfully!")
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void hasLiked(VideoRequest request, StreamObserver<VideoStatusResponse> responseObserver) {
        boolean hasLiked = videosService.hasLiked(request.getUserId(), request.getVideoId());
        VideoStatusResponse response = VideoStatusResponse.newBuilder()
                .setMessage(hasLiked ? "User has liked the video" : "User has not liked the video")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getVideosOfUser(VideoRequest request, StreamObserver<VideoListResponse> responseObserver) {
        List<Videos> videos = videosService.getVideosOfUser(request.getUserId(), "1");
        VideoListResponse.Builder responseBuilder = VideoListResponse.newBuilder();
        for (Videos video : videos) {
            VideoResponse videoResponse = VideoResponse.newBuilder()
                    .setVideoId(video.getVideoId())
                    .setUserId(video.getUserId())
                    .setTitle(video.getTitle())
                    .setDescription(video.getDescription())
                    .setCoverPath(video.getCoverPath())
                    .setVideoPath(video.getVideoPath())
                    .setLikes(video.getLikes())
                    .setComments(video.getComments())
                    .setCollections(video.getCollections())
                    .setShares(video.getShares())
                    .setUploadTime(video.getUploadTime().format(formatter))
                    .build();
            responseBuilder.addVideos(videoResponse);
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getCover(VideoRequest request, StreamObserver<VideoStatusResponse> responseObserver) {
        String coverPath = videosService.getCover(String.valueOf(request.getVideoId()));
        String localPath = "src/main/resources/videos/";

        File file = new File(localPath + coverPath);
        if (!file.exists()) {
            file = new File("src/main/resources/videos/默认封面/暂无封面.jpg");
        }

        try {
            byte[] imageBytes = Files.readAllBytes(file.toPath());
            String base64EncodedImage = Base64Utils.encodeToString(imageBytes);
            VideoStatusResponse response = VideoStatusResponse.newBuilder()
                    .setMessage("Cover retrieved successfully")
                    // .setCoverPath(base64EncodedImage) // This line should be removed as it doesn't exist in VideoStatusResponse
                    .build();
            responseObserver.onNext(response);
        } catch (IOException e) {
            responseObserver.onError(e);
        }
        responseObserver.onCompleted();
    }
}
