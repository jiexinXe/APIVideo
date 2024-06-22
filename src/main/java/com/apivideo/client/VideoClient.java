package com.apivideo.client;

import com.apivideo.entity.Videos;
import com.apivideo.grpc.VideoServiceGrpc;
import com.apivideo.grpc.VideosProto.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoClient {

    @GrpcClient("grpc-server")
    VideoServiceGrpc.VideoServiceBlockingStub blockingStub;

    public void addVideo(Videos videos) {
        VideoRequest request = VideoRequest.newBuilder()
                .setVideoId(videos.getVideoId())
                .setCollections(videos.getCollections())
                .setComments(videos.getComments())
                .setLikes(videos.getLikes())
                .setShares(videos.getShares())
                .setUserId(videos.getUserId())
                .setCoverPath(videos.getCoverPath())
                .setDescription(videos.getDescription())
                .setVideoPath(videos.getVideoPath())
                .setTitle(videos.getTitle())
                .setUploadTime(videos.getUploadTime().toString())
                .build();
        VideoStatusResponse response = this.blockingStub.addVideo(request);
    }

    public Videos getVideoById(int id) {
        VideoRequest request = VideoRequest.newBuilder()
                .setVideoId(id)
                .build();
        VideoResponse response = this.blockingStub.getVideoById(request);

        Videos video = new Videos()
                .setVideoPath(response.getVideoPath())
                .setShares(response.getShares())
                .setCollections(response.getCollections())
                .setVideoId(response.getVideoId())
                .setUploadTime(LocalDateTime.parse(response.getUploadTime()))
                .setLikes(response.getLikes())
                .setComments(response.getComments())
                .setCoverPath(response.getCoverPath())
                .setDescription(response.getDescription())
                .setTitle(response.getTitle())
                .setUserId(response.getUserId());
        return video;
    }

    public boolean deleteVideo(int id, Integer videoId) {
        VideoRequest request = VideoRequest.newBuilder()
                .setVideoId(id)
                .build();
        VideoStatusResponse response = this.blockingStub.deleteVideo(request);
        return true;
    }

    public void updateVideo(Videos videos) {
        VideoRequest request = VideoRequest.newBuilder()
                .setVideoId(videos.getVideoId())
                .setCollections(videos.getCollections())
                .setComments(videos.getComments())
                .setLikes(videos.getLikes())
                .setShares(videos.getShares())
                .setUserId(videos.getUserId())
                .setCoverPath(videos.getCoverPath())
                .setDescription(videos.getDescription())
                .setVideoPath(videos.getVideoPath())
                .setTitle(videos.getTitle())
                .setUploadTime(videos.getUploadTime().toString())
                .build();
        VideoStatusResponse response = this.blockingStub.addVideo(request);
    }

    public List<Videos> getRecommendedVideos(int userid) {
        VideoRequest request = VideoRequest.newBuilder()
                .setUserId(userid)
                .build();
        VideoListResponse listResponse = this.blockingStub.getRecommendedVideos(request);
        List<VideoResponse> Responselist = listResponse.getVideosList();
        List<Videos> list = new ArrayList<>();
        for (VideoResponse v : Responselist ){
            Videos video = new Videos()
                    .setVideoPath(v.getVideoPath())
                    .setShares(v.getShares())
                    .setCollections(v.getCollections())
                    .setVideoId(v.getVideoId())
                    .setUploadTime(LocalDateTime.parse(v.getUploadTime()))
                    .setLikes(v.getLikes())
                    .setComments(v.getComments())
                    .setCoverPath(v.getCoverPath())
                    .setDescription(v.getDescription())
                    .setTitle(v.getTitle())
                    .setUserId(v.getUserId());
            list.add(video);
        }
        return list;
    }

    public void likeVideo(int userid, int videoid) {
        VideoRequest request = VideoRequest.newBuilder()
                .setUserId(userid)
                .setVideoId(videoid)
                .build();
        VideoStatusResponse response = this.blockingStub.likeVideo(request);
    }

    public String hasLiked(int userid, int videoid) {
        VideoRequest request = VideoRequest.newBuilder()
                .setUserId(userid)
                .setVideoId(videoid)
                .build();
        VideoStatusResponse response = this.blockingStub.hasLiked(request);
        return response.getMessage();
    }

    public List<Videos> getVideosOfUser(int userid) {
        VideoRequest request = VideoRequest.newBuilder()
                .setUserId(userid)
                .build();
        VideoListResponse listResponse = this.blockingStub.getRecommendedVideos(request);
        List<VideoResponse> Responselist = listResponse.getVideosList();
        List<Videos> list = new ArrayList<>();
        for (VideoResponse v : Responselist ){
            Videos video = new Videos()
                    .setVideoPath(v.getVideoPath())
                    .setShares(v.getShares())
                    .setCollections(v.getCollections())
                    .setVideoId(v.getVideoId())
                    .setUploadTime(LocalDateTime.parse(v.getUploadTime()))
                    .setLikes(v.getLikes())
                    .setComments(v.getComments())
                    .setCoverPath(v.getCoverPath())
                    .setDescription(v.getDescription())
                    .setTitle(v.getTitle())
                    .setUserId(v.getUserId());
            list.add(video);
        }
        return list;
    }

    public String getCover(int videoid) {
        VideoRequest request = VideoRequest.newBuilder()
                .setVideoId(videoid)
                .build();
        VideoStatusResponse response = this.blockingStub.getCover(request);
        return response.getMessage();
    }
}
