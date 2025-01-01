package com.macro.mall.tiny.modules.mgs.dto;

import lombok.Data;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.mgs.dto
 * @Author: Pikachu
 * @CreateTime: 2024-12-31 22:52:47
 * @Description: 用于封装一个病房中有多少病床
 * @Version: 1.0
 */
@Data
public class BedCountOfRoomDTO {
    /**
     * 病房ID
     */
    private Integer roomId;
    /**
     * 病房名称
     */
    private String roomNum;
    /**
     * 病房名称
     */
    private String roomName;
    /**
     * 病房中可用病床数
     */
    private Integer availableBedCount;
}
