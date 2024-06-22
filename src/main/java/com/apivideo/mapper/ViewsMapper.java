package com.apivideo.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ViewsMapper {

    @Insert("INSERT INTO views (user_id, video_id) VALUES (#{userId}, #{videoId})")
    void insertViewedVideo(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

    @Select("SELECT video_id FROM views WHERE user_id = #{userId}")
    List<Integer> selectViewedVideoIds(@Param("userId") Integer userId);

    @Delete("DELETE FROM views WHERE video_id = #{videoId}")
    void deleteViewsByVideoId(@Param("videoId") Integer videoId);

    @Delete("DELETE FROM views WHERE user_id = #{userId}")
    void deleteViewsByUserId(@Param("userId") Integer userId);
}
