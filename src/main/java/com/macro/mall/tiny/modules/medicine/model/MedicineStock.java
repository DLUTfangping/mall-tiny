package com.macro.mall.tiny.modules.medicine.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 药材库存表
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MedicineStock对象", description = "药材库存表")
public class MedicineStock {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "药材ID")
    private Long drugId;

    @TableField(exist = false)
    @ApiModelProperty(value = "药材编码")
    private String drugCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "药材名称（用于显示）")
    private String drugName;

    @TableField(exist = false)
    @ApiModelProperty(value = "通用名（用于显示）")
    private String commonName;

    @TableField(exist = false)
    @ApiModelProperty(value = "药材类型（用于显示）")
    private String drugType;

    @TableField(exist = false)
    @ApiModelProperty(value = "处方药分类（用于显示）")
    private String prescriptionType;

    @TableField(exist = false)
    @ApiModelProperty(value = "药品分类（用于显示）")
    private String drugCategory;

    @TableField(exist = false)
    @ApiModelProperty(value = "剂型（用于显示）")
    private String dosageForm;

    @TableField(exist = false)
    @ApiModelProperty(value = "包装规格（用于显示）")
    private String spec;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否基本药物（用于显示）")
    private Integer isEssential;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否需要皮试（用于显示）")
    private Integer skinTestRequired;

    @TableField(exist = false)
    @ApiModelProperty(value = "生产厂家（用于显示）")
    private String manufacturer;

    @ApiModelProperty(value = "药房ID")
    private Long pharmacyId;

    @TableField(exist = false)
    @ApiModelProperty(value = "药房名称（用于显示）")
    private String pharmacyName;

    @ApiModelProperty(value = "批号")
    private String batchNo;

    @ApiModelProperty(value = "当前库存")
    private BigDecimal quantity;

    @TableField(exist = false)
    @ApiModelProperty(value = "最低预警库存")
    private BigDecimal minWarningStock;

    @ApiModelProperty(value = "生产日期")
    private Date productionDate;

    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}