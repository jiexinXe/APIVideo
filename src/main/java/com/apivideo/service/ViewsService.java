package com.apivideo.service;

import com.apivideo.entity.Views;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ViewsService {
    void addViewedVideo(Integer userId, Integer videoId);
    List<Integer> getViewedVideoIds(Integer userId);
    void deleteViewsByVideoId(Integer videoId);
}
