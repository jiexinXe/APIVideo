package com.apivideo.mapper;

import com.apivideo.entity.Likes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jiexinXe
 * @since 2024-05-31
 */
@Mapper
public interface LikesMapper extends BaseMapper<Likes> {
    @Select("SELECT COUNT(*) FROM likes WHERE user_id = #{userId} AND video_id = #{videoId}")
    int countByUserIdAndVideoId(@Param("userId") Integer userId, @Param("videoId") Integer videoId);
}
