package com.apivideo.service.impl;

import com.apivideo.entity.Likes;
import com.apivideo.entity.Videos;
import com.apivideo.mapper.LikesMapper;
import com.apivideo.mapper.VideosMapper;
import com.apivideo.service.VideosService;
import com.apivideo.service.ViewsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiexinXe
 * @since 2024-05-31
 */
@Service
public class VideosServiceImpl extends ServiceImpl<VideosMapper, Videos> implements VideosService {

    @Autowired
    private ViewsService viewsService;
    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private LikesMapper likesMapper;

    @Override
    public List<Videos> getRecommendedVideos(Integer userId, int limit) {
        if (userId == null) {
            // 未登录状态下推荐按点赞数排序的视频
            return videosMapper.selectRecommendedVideosWithoutExclusions(limit);
        } else {
            // 登录状态下推荐用户未观看的按点赞数排序的视频
            List<Integer> viewedVideoIds = viewsService.getViewedVideoIds(userId);
            if (viewedVideoIds.isEmpty()) {
                return videosMapper.selectRecommendedVideosWithoutExclusions(limit);
            } else {
                return videosMapper.selectRecommendedVideosWithExclusions(viewedVideoIds, limit);
            }
        }
    }

    @Override
    @Transactional
    public void likeVideo(Integer userId, Integer videoId) {
        // 更新视频的点赞数
        Videos video = this.getById(videoId);
        if (video != null) {
            video.setLikes(video.getLikes() + 1);
            this.updateById(video);

            // 在 likes 表中插入记录
            Likes like = new Likes();
            like.setUserId(userId);
            like.setVideoId(videoId);
            likesMapper.insert(like);
        }
    }

    @Override
    @Transactional
    public void unlikeVideo(Integer userId, Integer videoId) {
        // 更新视频的点赞数
        Videos video = this.getById(videoId);
        if (video != null) {
            video.setLikes(video.getLikes() - 1);
            this.updateById(video);

            // 从 likes 表中删除记录
            QueryWrapper<Likes> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("video_id", videoId);
            likesMapper.delete(queryWrapper);
        }
    }

    @Override
    public boolean hasLiked(Integer userId, Integer videoId) {
        int count = likesMapper.countByUserIdAndVideoId(userId, videoId);
        return count > 0;
    }

    @Override
    public List<Videos> getVideosOfUser(Integer userId, String page) {
        QueryWrapper<Videos> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("user_id", userId);
        return videosMapper.selectList(videoWrapper);
    }

    @Override
    public boolean deleteVideo(Integer videoUser, Integer deleteUser, Integer videoId) {
        // 根据视频id检索视频信息
        QueryWrapper<Videos> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("video_id", videoId);
        Videos video = videosMapper.selectOne(videoWrapper);

        // 对照视频拥有者id与删除者id，两者必须相同才能删除
        if (!Objects.equals(videoUser, video.getUserId()) || !Objects.equals(deleteUser, video.getUserId()))
            return false;

        // 删除视频
        videosMapper.delete(videoWrapper);
        return true;
    }
}
