package com.macro.mall.tiny.modules.mgs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.mgs.dto.MgsRoomsParam;
import com.macro.mall.tiny.modules.mgs.mapper.MgsRoomsMapper;
import com.macro.mall.tiny.modules.mgs.model.MgsRooms;
import com.macro.mall.tiny.modules.mgs.service.MgsRoomsService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 病房信息表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Service
public class MgsRoomsServiceImpl extends ServiceImpl<MgsRoomsMapper, MgsRooms> implements MgsRoomsService {
    @Override
    public boolean save(MgsRoomsParam param) {
        MgsRooms mgsRooms = new MgsRooms();
        BeanUtil.copyProperties(param, mgsRooms);
        return save(mgsRooms);
    }

    @Override
    public boolean removeByRoomNumber(String roomNumber) {
        boolean success = remove(new QueryWrapper<MgsRooms>(new MgsRooms()).eq("room_number", roomNumber));
        return success;
    }

    @Override
    public boolean updateByRoomNumber(MgsRoomsParam param) {
        MgsRooms mgsRooms = new MgsRooms();
        mgsRooms.setUpdatedAt(new Date());
        BeanUtil.copyProperties(param, mgsRooms);
        return update(mgsRooms, new QueryWrapper<MgsRooms>(new MgsRooms()).eq("room_number", param.getRoomNumber()));
    }

    @Override
    public Page<MgsRooms> list(Integer status, Integer pageSize, Integer pageNum) {
        QueryWrapper<MgsRooms> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MgsRooms> lambda = wrapper.lambda();
        lambda.eq(MgsRooms::getStatus, status);
        Page<MgsRooms> page = new Page<>(pageNum,pageSize);
        return page(page, wrapper);
    }
}
