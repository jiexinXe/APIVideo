package com.apivideo.service;

import com.apivideo.entity.Videos;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface VideosService extends IService<Videos> {
    List<Videos> getRecommendedVideos(String username, int limit);

    // 用户点赞
    void likeVideo(Integer userId, Integer videoId);
}
