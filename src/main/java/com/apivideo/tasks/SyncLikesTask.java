package com.apivideo.tasks;

import com.apivideo.mapper.VideosMapper;
import com.apivideo.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SyncLikesTask {

    @Autowired
    private RedisUtils redisUtil;

    @Autowired
    private VideosMapper videosMapper;

    @Scheduled(cron = "0 0 * * * ?") // 每小时执行一次
    public void syncLikesToDatabase() {
        // 获取所有点赞的 key
        Set<String> keys = redisUtil.keys("like:*");
        for (String key : keys) {
            String[] parts = key.split(":");
            Integer userId = Integer.parseInt(parts[1]);
            Integer videoId = Integer.parseInt(parts[2]);
            // 同步到数据库
            if (redisUtil.get(key) != null) {
                videosMapper.incrementLikes(videoId);
                redisUtil.delete(key);
            }
        }
    }
}
