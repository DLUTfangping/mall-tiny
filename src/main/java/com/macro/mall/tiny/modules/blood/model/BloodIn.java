package com.macro.mall.tiny.modules.blood.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 血液入库表
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
@Data
@TableName("blood_in")
@ApiModel(value = "BloodIn对象", description = "血液入库表")
public class BloodIn implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "入库单号")
    private String inNo;

    @ApiModelProperty(value = "血液类型")
    private String bloodType;

    @ApiModelProperty(value = "血型")
    private String bloodRhType;

    @ApiModelProperty(value = "血量(ml)")
    private Integer volume;

    @ApiModelProperty(value = "采血日期")
    private Date collectDate;

    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    @ApiModelProperty(value = "供血者")
    private String donor;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}