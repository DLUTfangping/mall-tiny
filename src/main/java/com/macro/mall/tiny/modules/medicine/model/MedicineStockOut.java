package com.macro.mall.tiny.modules.medicine.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 药材出库表
 * </p>
 *
 * @author macro
 * @since 2026-05-10
 */
@Data
@TableName("medicine_stock_out")
@ApiModel(value = "MedicineStockOut对象", description = "药材出库表")
public class MedicineStockOut implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "出库单号")
    private String outNo;

    @ApiModelProperty(value = "药房ID")
    private Long pharmacyId;

    @TableField(exist = false)
    @ApiModelProperty(value = "药房名称")
    private String pharmacyName;

    @ApiModelProperty(value = "出库方式：NORMAL-正常出库，ERROR-信息错误，EXPIRED-过期出库，DAMAGED-报损出库，INVENTORY-盘点出库")
    private String outType;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "出库总数量")
    private Integer totalQuantity;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态：0-草稿，1-已确认")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "出库明细列表")
    private List<MedicineStockOutDetail> details;
}