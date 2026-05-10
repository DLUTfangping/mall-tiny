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
 * 药房表
 * </p>
 *
 * @author macro
 * @since 2026-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("medicine_pharmacy")
@ApiModel(value="MedicinePharmacy对象", description="药房表")
public class MedicinePharmacy implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "药房名称")
    private String pharmacyName;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "状态：0=禁用，1=启用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否是默认药房：0=否，1=是")
    private Integer isDefault;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
