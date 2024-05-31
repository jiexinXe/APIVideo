package com.apivideo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("views")
@ApiModel(value = "Views对象", description = "")
public class Views implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "view_id", type = IdType.AUTO)
    private Integer viewId;

    @TableField("video_id")
    private Integer videoId;

    @TableField("user_id")
    private Integer userId;
}
