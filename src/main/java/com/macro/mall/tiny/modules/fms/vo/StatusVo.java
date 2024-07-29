package com.macro.mall.tiny.modules.fms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.fms.vo
 * @Author: Pikachu
 * @CreateTime: 2024-07-24 06:43:04
 * @Description: 状态值
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
public class StatusVo {

    private String des;

    private Integer val;
}
