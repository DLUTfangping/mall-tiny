package com.macro.mall.tiny.modules.fms.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.fms.vo
 * @Author: Pikachu
 * @CreateTime: 2024-07-18 13:59:33
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class StatisticsVo {
    @ApiModelProperty(value = "总金额")
    private String totalAmount;
    @ApiModelProperty(value = "有效的金额")
    private String validAmount;
    @ApiModelProperty(value = "无效的金额")
    private String invalidAmount;
    @ApiModelProperty(value = "当日打车费")
    private List<DateAmount> dateAmountList;
    @Data
    public static class DateAmount {
        @ApiModelProperty(value = "日期")
        private String date;
        @ApiModelProperty(value = "当天的金额")
        private String amount;
        // 14:03-14:07  15-30-15:40
        @ApiModelProperty(value = "时间集合")
        private String curDetailTime;
        @ApiModelProperty(value = "发票集合")
        private String curInvoices;

    }
}
