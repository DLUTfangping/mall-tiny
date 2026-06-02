package com.macro.mall.tiny.modules.medicine.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 有效期预警颜色配置表
 * </p>
 *
 * @author macro
 * @since 2026-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("medicine_validity_warning_config")
@ApiModel(value="MedicineValidityWarningConfig对象", description="有效期预警颜色配置表")
public class MedicineValidityWarningConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "级别编码")
    private String levelCode;

    @ApiModelProperty(value = "级别名称")
    private String levelName;

    @ApiModelProperty(value = "最小天数（含）")
    private Integer minDays;

    @ApiModelProperty(value = "最大天数（含），-1表示无上限")
    private Integer maxDays;

    @ApiModelProperty(value = "显示颜色")
    private String color;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否启用")
    private Integer enabled;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}