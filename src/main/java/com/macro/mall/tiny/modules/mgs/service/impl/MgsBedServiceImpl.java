package com.macro.mall.tiny.modules.mgs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.mgs.dto.MgsBedParam;
import com.macro.mall.tiny.modules.mgs.mapper.MgsBedMapper;
import com.macro.mall.tiny.modules.mgs.model.MgsBed;
import com.macro.mall.tiny.modules.mgs.model.MgsRooms;
import com.macro.mall.tiny.modules.mgs.service.MgsBedService;
import com.macro.mall.tiny.modules.mgs.service.MgsRoomsService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 床位信息表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Service
public class MgsBedServiceImpl extends ServiceImpl<MgsBedMapper, MgsBed> implements MgsBedService {
    @Lazy
    @Resource
    private MgsRoomsService mgsRoomsService;
    @Override
    public CommonResult save(MgsBedParam param) {
        // 判断添加病床是否会超限
        Integer roomId = param.getRoomId();
        String roomNumber = param.getRoomNumber();
        MgsRooms mgsRooms = mgsRoomsService.getOne(new LambdaQueryWrapper<MgsRooms>(new MgsRooms()).eq(MgsRooms::getId, roomId), false);
        Integer capacity = mgsRooms.getCapacity();
        // 查询出病房中现有病床的数量，包含启用禁用的
        Integer enableNum = countBedsByRoomAndStatus(roomNumber, null);
        if (++enableNum > capacity) return CommonResult.failed("房间容量不足，无法新增");

        // 获取病房中最大病床编号
        MgsBed maxMgsBed = getOne(new LambdaQueryWrapper<MgsBed>(new MgsBed())
                .eq(MgsBed::getRoomId, roomId).orderByDesc(MgsBed::getBedNumber),false);
        // 病房编号sr-1 病床编号sr-1-1  sr-1-2
        if (maxMgsBed != null) {
            // 使用hu-tools中的工具类获取字符串"-"后的数字
            String[] split = maxMgsBed.getBedNumber().split("-");
            String dep = split[0];
            String maxNum = split[2];
            int maxBedNumberInt = Integer.parseInt(maxNum);
            maxBedNumberInt++;
            param.setBedNumber(param.getRoomNumber() + "-" +maxBedNumberInt);
        } else {
            param.setBedNumber(param.getRoomNumber() + "-1");
        }
        MgsBed mgsBed = new MgsBed();
        BeanUtil.copyProperties(param, mgsBed);
        boolean res = save(mgsBed);
        if (res) return CommonResult.success(null);
       return CommonResult.failed("新增失败");
    }

    @Override
    public boolean removeByBedNumber(String bedNumber) {
        boolean success = remove(new QueryWrapper<MgsBed>(new MgsBed()).eq("bed_number", bedNumber));
        return success;
    }

    @Override
    public boolean updateByBedNumber(MgsBedParam param) {
        MgsBed mgsBed = new MgsBed();
        mgsBed.setUpdatedAt(new Date());
        BeanUtil.copyProperties(param, mgsBed);
        return update(mgsBed, new QueryWrapper<MgsBed>(new MgsBed()).eq("bed_number", param.getBedNumber()));
    }

    @Override
    public Page<MgsBed> list(Integer status, Integer roomId, Integer pageSize, Integer pageNum) {
        LambdaQueryWrapper<MgsBed> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            lambdaQueryWrapper.eq(MgsBed::getStatus, status);
        }
        if (roomId != null) {
            lambdaQueryWrapper.eq(MgsBed::getRoomId, roomId);
        }
        Page<MgsBed> page = new Page<>(pageNum,pageSize);
        return page(page, lambdaQueryWrapper);
    }

    /**
     * 根据 roomNum 和 status 统计病床数量
     *
     * @param roomNum 病房编号
     * @param status 床位状态
     * @return 满足条件的病床数量
     */
    @Override
    public int countBedsByRoomAndStatus(String roomNum, Integer status) {
        // 构造查询条件
        QueryWrapper<MgsBed> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_number", roomNum); // 查询 roomNum
        if (status != null) {
            queryWrapper.eq("status", status); // 查询 status
        }
        int c = (int)count(queryWrapper);
        // 使用 count 方法统计数量
        return c ;
    }
}
