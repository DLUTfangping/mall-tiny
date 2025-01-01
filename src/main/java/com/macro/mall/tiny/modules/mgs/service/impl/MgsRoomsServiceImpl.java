package com.macro.mall.tiny.modules.mgs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.enums.CommonStatus;
import com.macro.mall.tiny.modules.mgs.dto.MgsRoomsParam;
import com.macro.mall.tiny.modules.mgs.mapper.MgsRoomsMapper;
import com.macro.mall.tiny.modules.mgs.model.MgsRooms;
import com.macro.mall.tiny.modules.mgs.service.MgsBedService;
import com.macro.mall.tiny.modules.mgs.service.MgsRoomsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private MgsBedService mgsBedService;
    @Override
    public boolean save(MgsRoomsParam param) {
        // 根据departmentId查询roomNumber排序最大的值
        MgsRooms maxRoomNumber = getOne(new LambdaQueryWrapper<MgsRooms>(new MgsRooms())
                .eq(MgsRooms::getDepartmentId, param.getDepartmentNum()).orderByDesc(MgsRooms::getRoomNumber),false);
        // 在roomNumber基础上加1
        if (maxRoomNumber != null) {
            // 使用hu-tools中的工具类获取字符串"-"后的数字  sr-1  sr-2
            String[] split = maxRoomNumber.getRoomNumber().split("-");
            String dep = split[0];
            String maxNum = split[1];
            int maxRoomNumberInt = Integer.parseInt(maxNum);
            maxRoomNumberInt++;
            param.setRoomNumber(dep + "-" +maxRoomNumberInt);
        } else {
            param.setRoomNumber(param.getShortCode() + "-1");
        }
        MgsRooms mgsRooms = new MgsRooms();
        BeanUtil.copyProperties(param, mgsRooms);
        return save(mgsRooms);
    }

    @Override
    public boolean removeByRoomNumber(String roomNumber) {
        boolean success = remove(new LambdaQueryWrapper<MgsRooms>(new MgsRooms()).eq(MgsRooms::getRoomNumber, roomNumber));
        return success;
    }

    @Override
    public boolean updateByRoomNumber(MgsRoomsParam param) {
        MgsRooms mgsRooms = new MgsRooms();
        mgsRooms.setUpdatedAt(new Date());
        BeanUtil.copyProperties(param, mgsRooms);
        return update(mgsRooms, new LambdaQueryWrapper<MgsRooms>(new MgsRooms()).eq(MgsRooms::getRoomNumber, param.getRoomNumber()));
    }

    @Override
    public Page<MgsRooms> list(Integer status, Integer departmentId, Integer pageSize, Integer pageNum) {
        QueryWrapper<MgsRooms> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MgsRooms> lambda = wrapper.lambda();
        if (status != null) {
            lambda.eq(MgsRooms::getStatus, status);
        }
        lambda.eq(MgsRooms::getDepartmentId, departmentId);
        Page<MgsRooms> page = new Page<>(pageNum,pageSize);
        Page<MgsRooms> resultPage = page(page, wrapper);
        // 遍历查询结果，设置 enableNum 字段
        for (MgsRooms room : resultPage.getRecords()) {
            // 假设 enableNum 的值需要通过某种计算方式获取
            int enableNum = mgsBedService.countBedsByRoomAndStatus(room.getRoomNumber(), CommonStatus.ACTIVE.getCode());
            room.setEnableNum(enableNum);
        }
        return resultPage;
    }

    @Override
    public boolean checkRoom(Integer roomId) {
        return getById(roomId) != null;
    }



}
