package com.macro.mall.tiny.modules.ums.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.ums.dto
 * @Author: Pikachu
 * @CreateTime: 2024-07-28 22:45:10
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class InvoiceStatusParam {
    @ApiModelProperty("要操作的开始日期")
    private Date startDate;
    @ApiModelProperty("要操作的结束日期")
    private Date endDate;
    @ApiModelProperty("要操作的状态")
    private Integer status;
}
