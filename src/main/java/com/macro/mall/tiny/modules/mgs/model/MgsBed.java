package com.macro.mall.tiny.modules.mgs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 床位信息表
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Getter
@Setter
@TableName("mgs_bed")
@ApiModel(value = "MgsBed对象", description = "床位信息表")
public class MgsBed implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("床位唯一标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("关联 Room 表")
    private String roomNumber;

    @ApiModelProperty("床位编号")
    private String bedNumber;

    @ApiModelProperty("床位名称")
    private String bedName;

    @ApiModelProperty("床位状态（0：可用，1：占用，2：删除）")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;


}
