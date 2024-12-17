package com.macro.mall.tiny.modules.mgs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.mgs.dto.MgsRoomsParam;
import com.macro.mall.tiny.modules.mgs.model.MgsRooms;

/**
 * <p>
 * 病房信息表 服务类
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */

public interface MgsRoomsService extends IService<MgsRooms> {

    boolean save(MgsRoomsParam param);

    boolean removeByRoomNumber(String roomNumber);

    boolean updateByRoomNumber(MgsRoomsParam param);

    Page<MgsRooms> list(Integer status, String departmentNum,Integer pageSize, Integer pageNum);
}
