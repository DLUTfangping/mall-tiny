package com.macro.mall.tiny.modules.medicine.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("medicine_drug_usage")
@ApiModel(value="MedicineDrugUsage对象", description="药品用法表")
public class MedicineDrugUsage implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "药品编码")
    private String drugCode;
    @TableField("`usage`")
    @ApiModelProperty(value = "用法")
    private String usage;
    @ApiModelProperty(value = "频次")
    private String frequency;
    @ApiModelProperty(value = "单次剂量")
    private BigDecimal singleDose;
    @ApiModelProperty(value = "剂量单位")
    private String doseUnit;
    @ApiModelProperty(value = "日最大剂量")
    private BigDecimal maxDailyDose;
    @ApiModelProperty(value = "适用年龄范围")
    private String ageRange;
    @ApiModelProperty(value = "禁忌症")
    private String contraindication;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "状态：0=禁用，1=启用")
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
