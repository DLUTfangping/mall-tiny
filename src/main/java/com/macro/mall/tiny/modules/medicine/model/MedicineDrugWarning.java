package com.macro.mall.tiny.modules.medicine.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 药品预警表
 * </p>
 *
 * @author macro
 * @since 2026-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("medicine_drug_warning")
@ApiModel(value="MedicineDrugWarning对象", description="药品预警表")
public class MedicineDrugWarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "药品编码")
    private String drugCode;

    @ApiModelProperty(value = "最低预警库存量")
    private BigDecimal minWarningStock;

    @ApiModelProperty(value = "最高预警库存量")
    private BigDecimal maxWarningStock;

    @ApiModelProperty(value = "有效期预警天数（提前N天预警）")
    private Integer validityWarningDays;

    @ApiModelProperty(value = "是否启用有效期预警：0=否，1=是")
    private Integer validityWarningEnabled;

    @ApiModelProperty(value = "是否启用批号预警：0=否，1=是")
    private Integer batchWarningEnabled;

    @ApiModelProperty(value = "批号预警阈值（库存低于此值时触发预警）")
    private BigDecimal batchWarningThreshold;

    @ApiModelProperty(value = "再订货点")
    private BigDecimal reorderPoint;

    @ApiModelProperty(value = "盘点周期（天）")
    private Integer inspectionCycle;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
