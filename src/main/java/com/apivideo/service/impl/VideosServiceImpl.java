package com.apivideo.service.impl;

import com.apivideo.entity.Videos;
import com.apivideo.mapper.VideosMapper;
import com.apivideo.service.VideosService;
import com.apivideo.service.ViewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideosServiceImpl extends ServiceImpl<VideosMapper, Videos> implements VideosService {

    @Autowired
    private ViewsService viewsService;

    @Override
    public List<Videos> getRecommendedVideos(String username, int limit) {
        if (username == null) {
            // 未登录状态下推荐按点赞数排序的视频
            return this.lambdaQuery().orderByDesc(Videos::getLikes).last("LIMIT " + limit).list();
        } else {
            // 登录状态下推荐用户未观看的按点赞数排序的视频
            List<Integer> viewedVideoIds = viewsService.getViewedVideoIds(username);
            return this.lambdaQuery().notIn(Videos::getVideoId, viewedVideoIds).orderByDesc(Videos::getLikes).last("LIMIT " + limit).list();
        }
    }
}
