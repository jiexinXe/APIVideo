package com.apivideo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiexinXe
 * @since 2024-05-31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("videos")
@ApiModel(value = "Videos对象", description = "")
public class Videos implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "video_id", type = IdType.AUTO)
    private Integer videoId;

    @TableField("user_id")
    private Integer userId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("cover_path")
    private String coverPath;

    @TableField("video_path")
    private String videoPath;

    @TableField("likes")
    private Integer likes;

    @TableField("comments")
    private Integer comments;

    @TableField("collections")
    private Integer collections;

    @TableField("shares")
    private Integer shares;

    @TableField("upload_time")
    private LocalDateTime uploadTime;
}
