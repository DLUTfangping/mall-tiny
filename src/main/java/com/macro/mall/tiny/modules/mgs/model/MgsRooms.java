package com.macro.mall.tiny.modules.mgs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 病房信息表
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Getter
@Setter
@TableName("mgs_rooms")
@ApiModel(value = "MgsRooms对象", description = "病房信息表")
public class MgsRooms implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("病房唯一标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("关联 Department 表")
    private String departmentNum;

    @ApiModelProperty("关联 Department 表 id 字段")
    private Integer departmentId;

    @ApiModelProperty("病房编号")
    private String roomNumber;

    @ApiModelProperty("病房名称")
    private String roomName;

    @ApiModelProperty("病房床位数")
    private Integer capacity;

    @ApiModelProperty("启用的床位数")
//    添加注释说明该字段不是数据库字段
    @TableField(exist = false)
    private Integer enableNum;

    @ApiModelProperty("病房状态（0：可用，1：占用，2：删除）")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;

}
