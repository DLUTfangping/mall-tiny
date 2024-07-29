package com.macro.mall.tiny.modules.fms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.fms.dto
 * @Author: Pikachu
 * @CreateTime: 2024-07-11 07:36:43
 * @Description: TODO
 * @Version: 1.0
 */
@Data
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InvoiceParam {
    @ApiModelProperty("主键")
    private Integer id;

    @NotBlank(message = "发票号码不能为空")
    @Size(max = 8, min = 8, message = "发票号码长度为8")
    @ApiModelProperty("发票号码")
    private String invoiceNumber;

    @NotNull(message = "发票日期不能为空")
    @PastOrPresent(message = "发票日期不能是未来的日期")
    @ApiModelProperty("发票日期")
    private Date invoiceDate;

    @NotBlank(message = "车牌号不能为空")
    @ApiModelProperty("车牌号")
    private String licensePlate;

    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @DecimalMin(value = "0.0", inclusive = false, message = "里程数必须大于0")
    @ApiModelProperty("里程数")
    private BigDecimal mileage;

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "金额必须大于0")
    @ApiModelProperty("金额")
    private BigDecimal amount;
}
