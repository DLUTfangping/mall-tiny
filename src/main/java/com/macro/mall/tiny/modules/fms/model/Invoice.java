package com.macro.mall.tiny.modules.fms.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author macro
 * @since 2024-07-09
 */
@Getter
@Setter
@ApiModel(value = "Invoice对象", description = "")
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    // 状态值
    public static final  Integer STATUS_ALL = 2;
    public static final  Integer STATUS_NORMAL = 1;
    public static final  Integer STATUS_UN_NORMAL = 0;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发票号码")
    private String invoiceNumber;

    @ApiModelProperty("开票日期")
    private Date invoiceDate;

    @ApiModelProperty("车牌号")
    private String licensePlate;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("里程数")
    private BigDecimal mileage;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("状态：0-删除，1-未删除")
//    @TableLogic
    private Integer status;
}
