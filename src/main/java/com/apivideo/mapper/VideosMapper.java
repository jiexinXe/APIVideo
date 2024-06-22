package com.apivideo.mapper;

import com.apivideo.entity.Videos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
            "WHERE ",
            "  <if test='excludedVideoIds != null and excludedVideoIds.size() > 0'>",
            "    video_id NOT IN ",
            "    <foreach item='id' collection='excludedVideoIds' open='(' separator=',' close=')'>",
            "      #{id}",
            "    </foreach>",
            "  </if>",
            "  <if test='excludedVideoIds == null or excludedVideoIds.size() == 0'>",
            "    1 = 0", // 确保返回空结果集
            "  </if>",
            "ORDER BY likes DESC LIMIT #{limit}",
            "</script>"
    })
    List<Videos> selectRecommendedVideosWithExclusions(@Param("excludedVideoIds") List<Integer> excludedVideoIds, @Param("limit") int limit);


    @Update("UPDATE videos SET likes = likes + 1 WHERE video_id = #{videoId}")
    void incrementLikes(Integer videoId);

    @Update("UPDATE videos SET likes = likes - 1 WHERE video_id = #{videoId}")
    void decrementLikes(Integer videoId);
}
