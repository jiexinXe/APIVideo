package com.apivideo.service.impl;

import com.apivideo.entity.Comments;
import com.apivideo.mapper.CommentsMapper;
import com.apivideo.service.CommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiexinXe
 * @since 2024-05-31
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {

}
