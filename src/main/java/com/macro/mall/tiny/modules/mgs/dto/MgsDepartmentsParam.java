package com.macro.mall.tiny.modules.mgs.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.mgs.dto
 * @Author: Pikachu
 * @CreateTime: 2024-11-23 09:13:59
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class MgsDepartmentsParam {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("组室唯一标识")
        private Integer id;

        @ApiModelProperty("组室编号，唯一索引")
        private String departmentNum;

        @ApiModelProperty("组室名称（如内科、外科）")
        private String name;

        @ApiModelProperty("组室状态（0：有效，1：无效，2：删除）")
        private Integer status;

        @ApiModelProperty("创建时间")
        private Date createdAt;

        @ApiModelProperty("更新时间")
        private Date updatedAt;
}
