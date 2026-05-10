package com.macro.mall.tiny.modules.medicine.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 药材入库单主表
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MedicineStockIn对象", description = "药材入库单主表")
public class MedicineStockIn {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "入库单号")
    private String inNo;

    @ApiModelProperty(value = "入库药房ID")
    private Long pharmacyId;

    @ApiModelProperty(value = "药房名称（用于显示）")
    private String pharmacyName;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "状态：0=草稿，1=已入库")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "入库明细列表")
    private List<MedicineStockInDetail> details;
}