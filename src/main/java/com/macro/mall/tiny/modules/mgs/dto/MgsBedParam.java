package com.macro.mall.tiny.modules.mgs.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.mgs.dto
 * @Author: Pikachu
 * @CreateTime: 2024-11-24 15:59:47
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class MgsBedParam {
    @ApiModelProperty("床位唯一标识")
    private Integer id;

    @ApiModelProperty("关联 Room 表")
    private String roomNumber;

    @ApiModelProperty("关联 Room 表 id字段")
    private Integer roomId;

    @ApiModelProperty("床位编号")
    private String bedNumber;

    @ApiModelProperty("床位名称")
    private String bedName;

    @ApiModelProperty("床位状态（0：可用，1：占用，2：删除）")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;

    public String getShortCode() {
        if (this.bedNumber == null) return "";
        String[] split = this.bedNumber.split("-");
        return split[0];
    }
}
