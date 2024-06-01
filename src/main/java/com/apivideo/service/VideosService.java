package com.apivideo.service;

import com.apivideo.entity.Videos;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface VideosService extends IService<Videos> {
    List<Videos> getRecommendedVideos(String username, int limit);

    // 用户点赞
    void likeVideo(Integer userId, Integer videoId);

    // 该视频用户是否已经点赞
    boolean hasLiked(Integer userId, Integer videoId);

    List<Videos> getVideosOfUser(Integer userid);
    boolean deleteVideo(Integer video_user, Integer delete_user, Integer video_id);
}
