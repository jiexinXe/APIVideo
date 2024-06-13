package com.apivideo.service.impl;

import com.apivideo.mapper.ViewsMapper;
import com.apivideo.service.ViewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewsServiceImpl implements ViewsService {

    @Autowired
    private ViewsMapper viewsMapper;

    @Override
    public void addViewedVideo(Integer userId, Integer videoId) {
        viewsMapper.insertViewedVideo(userId, videoId);
    }

    @Override
    public List<Integer> getViewedVideoIds(Integer userId) {
        return viewsMapper.selectViewedVideoIds(userId);
    }

    @Override
    public void deleteViewsByVideoId(Integer videoId){
        viewsMapper.deleteViewsByVideoId(videoId);
    }
}

