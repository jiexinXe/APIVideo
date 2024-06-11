package com.apivideo.service;

import com.apivideo.entity.Videos;
import com.apivideo.utils.Rest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface VideosService extends IService<Videos> {
    List<Videos> getRecommendedVideos(Integer userId, int limit);
    List<Videos> getRandomVideos(int limit);

    // 用户点赞
    void likeVideo(Integer userId, Integer videoId);

    // 该视频用户是否已经点赞
    boolean hasLiked(Integer userId, Integer videoId);

    // 取消点赞
    void unlikeVideo(Integer userId, Integer videoId);

    List<Videos> getVideosOfUser(Integer userid, String page);
    boolean deleteVideo(Integer deleteUser, Integer videoId);
    String getCover(String videoid);
}
