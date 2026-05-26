package com.macro.mall.tiny.modules.medicine.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 药材出库明细表
 * </p>
 *
 * @author macro
 * @since 2026-05-12
 */
@Data
@TableName("medicine_stock_out_detail")
@ApiModel(value = "MedicineStockOutDetail对象", description = "药材出库明细表")
public class MedicineStockOutDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "出库单ID")
    private Long outId;

    @ApiModelProperty(value = "药品ID")
    private Long drugId;

    @ApiModelProperty(value = "药品编码")
    private String drugCode;

    @ApiModelProperty(value = "药品名称")
    private String drugName;

    @ApiModelProperty(value = "通用名")
    private String commonName;

    @ApiModelProperty(value = "药材类型")
    private String drugType;

    @ApiModelProperty(value = "处方药分类")
    private String prescriptionType;

    @ApiModelProperty(value = "药品分类")
    private String drugCategory;

    @ApiModelProperty(value = "剂型")
    private String dosageForm;

    @ApiModelProperty(value = "包装规格")
    private String spec;

    @ApiModelProperty(value = "基本药物")
    private Integer isEssential;

    @ApiModelProperty(value = "需要皮试")
    private Integer skinTestRequired;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "出库数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "出库单位：PACK-包装单位，BASE-基本单位")
    private String unitType;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "包装单位")
    private String unit;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "批号")
    private String batchNo;

    @ApiModelProperty(value = "生产日期")
    private String productionDate;

    @ApiModelProperty(value = "有效期")
    private String expiryDate;
}