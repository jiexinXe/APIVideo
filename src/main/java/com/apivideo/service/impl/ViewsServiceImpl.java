package com.apivideo.service.impl;

import com.apivideo.entity.Views;
import com.apivideo.mapper.ViewsMapper;
import com.apivideo.service.ViewsService;
import com.apivideo.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ViewsServiceImpl extends ServiceImpl<ViewsMapper, Views> implements ViewsService {

    @Autowired
    private UsersService usersService;

    @Override
    public List<Integer> getViewedVideoIds(String username) {
        Integer userId = usersService.findByUsername(username).getUserId();
        return this.lambdaQuery().eq(Views::getUserId, userId).list()
                .stream().map(Views::getVideoId).collect(Collectors.toList());
    }
}
