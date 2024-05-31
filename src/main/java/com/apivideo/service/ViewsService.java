package com.apivideo.service;

import com.apivideo.entity.Views;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ViewsService extends IService<Views> {
    List<Integer> getViewedVideoIds(String username);
}
