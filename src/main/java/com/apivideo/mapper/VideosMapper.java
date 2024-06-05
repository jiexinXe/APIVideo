package com.apivideo.mapper;

import com.apivideo.entity.Videos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface VideosMapper extends BaseMapper<Videos> {

    @Select("SELECT video_id, user_id, title, description, cover_path, video_path, likes, comments, collections, shares, upload_time " +
            "FROM videos " +
            "ORDER BY likes DESC LIMIT #{limit}")
    List<Videos> selectRecommendedVideosWithoutExclusions(@Param("limit") int limit);

    @Select({
            "<script>",
            "SELECT video_id, user_id, title, description, cover_path, video_path, likes, comments, collections, shares, upload_time ",
            "FROM videos ",
            "WHERE video_id NOT IN ",
            "<foreach item='id' collection='excludedVideoIds' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach> ",
            "ORDER BY likes DESC LIMIT #{limit}",
            "</script>"
    })
    List<Videos> selectRecommendedVideosWithExclusions(@Param("excludedVideoIds") List<Integer> excludedVideoIds, @Param("limit") int limit);
}
