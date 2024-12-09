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
 * 组室信息表
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Getter
@Setter
@TableName("mgs_departments")
@ApiModel(value = "MgsDepartments对象", description = "组室信息表")
public class MgsDepartments implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("组室唯一标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("组室编号，唯一索引")
    private String departmentNum;

    @ApiModelProperty("组室名称（如内科、外科）")
    private String name;

    @ApiModelProperty("组室状态（0：有效，1：无效，2：删除）")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;


}
