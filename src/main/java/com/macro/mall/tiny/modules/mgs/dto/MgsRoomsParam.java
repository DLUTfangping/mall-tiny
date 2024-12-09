package com.macro.mall.tiny.modules.mgs.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.mgs.dto
 * @Author: Pikachu
 * @CreateTime: 2024-11-24 15:39:09
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class MgsRoomsParam {

    @ApiModelProperty("病房唯一标识")
    private Integer id;

    @ApiModelProperty("关联 Department 表")
    private String departmentNum;

    @ApiModelProperty("病房编号")
    private String roomNumber;

    @ApiModelProperty("病房名称")
    private String roomName;

    @ApiModelProperty("病房床位数")
    private Integer capacity;

    @ApiModelProperty("病房状态（0：可用，1：占用，2：删除）")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    private Date updatedAt;
}
