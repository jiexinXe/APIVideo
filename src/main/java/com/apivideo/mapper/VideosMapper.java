package com.apivideo.mapper;

import com.apivideo.entity.Videos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VideosMapper extends BaseMapper<Videos> {

    @Select("SELECT * FROM Videos ORDER BY likes DESC LIMIT #{offset}, #{size}")
    List<Videos> getTopVideos(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * FROM Videos WHERE video_id NOT IN (SELECT video_id FROM Views WHERE user_id = (SELECT user_id FROM Users WHERE username = #{username})) ORDER BY likes DESC LIMIT #{offset}, #{size}")
    List<Videos> getPersonalizedRecommendations(@Param("username") String username, @Param("offset") int offset, @Param("size") int size);
}
