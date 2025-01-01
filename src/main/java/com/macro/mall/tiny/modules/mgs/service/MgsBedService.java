package com.macro.mall.tiny.modules.mgs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.mgs.dto.MgsBedParam;
import com.macro.mall.tiny.modules.mgs.model.MgsBed;

/**
 * <p>
 * 床位信息表 服务类
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
public interface MgsBedService extends IService<MgsBed> {

    CommonResult save(MgsBedParam param);

    boolean removeByBedNumber(String bedNumber);

    boolean updateByBedNumber(MgsBedParam param);

    Page<MgsBed> list(Integer status, Integer roomId, Integer pageSize, Integer pageNum);

    int countBedsByRoomAndStatus(String roomNum, Integer status);
}
