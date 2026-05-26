package com.macro.mall.tiny.modules.medicine.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 药材入库单明细表
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MedicineStockInDetail对象", description = "药材入库单明细表")
public class MedicineStockInDetail {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "入库单ID")
    private Long inId;

    @ApiModelProperty(value = "药材ID")
    private Long drugId;

    @ApiModelProperty(value = "药材编码")
    private String drugCode;

    @ApiModelProperty(value = "药材名称（用于显示）")
    private String drugName;

    @ApiModelProperty(value = "通用名（用于显示）")
    private String commonName;

    @ApiModelProperty(value = "药材类型（用于显示）")
    private String drugType;

    @ApiModelProperty(value = "处方药分类（用于显示）")
    private String prescriptionType;

    @ApiModelProperty(value = "药品分类（用于显示）")
    private String drugCategory;

    @ApiModelProperty(value = "剂型（用于显示）")
    private String dosageForm;

    @ApiModelProperty(value = "包装规格（用于显示）")
    private String spec;

    @ApiModelProperty(value = "是否基本药物（用于显示）")
    private Integer isEssential;

    @ApiModelProperty(value = "是否需要皮试（用于显示）")
    private Integer skinTestRequired;

    @ApiModelProperty(value = "生产厂家（用于显示）")
    private String manufacturer;

    @ApiModelProperty(value = "批号")
    private String batchNo;

    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date productionDate;

    @ApiModelProperty(value = "有效期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expiryDate;

    @ApiModelProperty(value = "入库数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "包装单位")
    private String unit;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}