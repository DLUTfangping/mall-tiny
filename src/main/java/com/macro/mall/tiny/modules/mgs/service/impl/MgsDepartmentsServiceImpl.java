package com.macro.mall.tiny.modules.mgs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.enums.CommonStatus;
import com.macro.mall.tiny.modules.mgs.dto.BedCountOfRoomDTO;
import com.macro.mall.tiny.modules.mgs.dto.MgsDepartmentsParam;
import com.macro.mall.tiny.modules.mgs.mapper.MgsDepartmentsMapper;
import com.macro.mall.tiny.modules.mgs.model.MgsBed;
import com.macro.mall.tiny.modules.mgs.model.MgsDepartments;
import com.macro.mall.tiny.modules.mgs.model.MgsRooms;
import com.macro.mall.tiny.modules.mgs.service.MgsBedService;
import com.macro.mall.tiny.modules.mgs.service.MgsDepartmentsService;
import com.macro.mall.tiny.modules.mgs.service.MgsRoomsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 组室信息表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Slf4j
@Service
public class MgsDepartmentsServiceImpl extends ServiceImpl<MgsDepartmentsMapper, MgsDepartments> implements MgsDepartmentsService {

    @Resource
    private MgsRoomsService mgsRoomsService;

    @Resource
    private MgsBedService mgsBedService;

    @Override
    public boolean save(MgsDepartmentsParam param) {
        MgsDepartments mgsDepartments = new MgsDepartments();
        BeanUtil.copyProperties(param, mgsDepartments);
        return save(mgsDepartments);
    }

    @Override
    public boolean removeById(Integer id) {
        boolean success;
        if (id < 0) {
            success = remove(null);
        } else {
            success = remove(new QueryWrapper<MgsDepartments>(new MgsDepartments()).eq("id", id));
        }
        return success;
    }

    @Override
    public boolean updateById(MgsDepartmentsParam param) {
        MgsDepartments mgsDepartments = new MgsDepartments();
        mgsDepartments.setUpdatedAt(new Date());
        BeanUtil.copyProperties(param, mgsDepartments);
        return updateById(mgsDepartments);
    }

    @Override
    public Page<MgsDepartments> list(Integer status, Integer pageSize, Integer pageNum) {
        QueryWrapper<MgsDepartments> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MgsDepartments> lambda = wrapper.lambda();
        lambda.eq(MgsDepartments::getStatus, status);
        Page<MgsDepartments> page = new Page<>(pageNum,pageSize);
        return page(page, wrapper);
    }

    public MgsDepartments getByNameStatus(String name, Integer status) {
        LambdaQueryWrapper<MgsDepartments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MgsDepartments::getName, name)
                .eq(MgsDepartments::getStatus, status);
        return getOne(queryWrapper);
    }

    @Override
    public boolean checkDepartment(Integer departmentId) {
        LambdaQueryWrapper<MgsDepartments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MgsDepartments::getId, departmentId)
                .eq(MgsDepartments::getStatus, CommonStatus.ACTIVE.getCode());
        if (count(queryWrapper) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<BedCountOfRoomDTO> getBedCountOfRoomList(Integer departmentId) {
        List<BedCountOfRoomDTO> bedCountOfRoomList = new ArrayList<>();
        // 根据组室ID查询该组室是否可用
        MgsDepartments mgsDepartments = getById(departmentId);
        if (mgsDepartments == null || mgsDepartments.getStatus() != CommonStatus.ACTIVE.getCode()) {
            log.warn("组室不存在或状态不可用");
            return new ArrayList<>();
        }
        // 根据组室ID去病房表中查询有哪些病房
        LambdaQueryWrapper<MgsRooms> queryRoomWrapper = new LambdaQueryWrapper<>();
        queryRoomWrapper.eq(MgsRooms::getDepartmentId, mgsDepartments.getId());
        queryRoomWrapper.eq(MgsRooms::getStatus, CommonStatus.ACTIVE.getCode());
        List<MgsRooms> roomList = mgsRoomsService.list(queryRoomWrapper);
        if (CollUtil.isEmpty(roomList)) {
            log.info("组室中病房数量为空");
            return new ArrayList<>();
        }
        List<Integer> roomIds = roomList.stream().map(MgsRooms::getId).collect(Collectors.toList());
        // 在病房中查找剩余的病床数状态为有效（未删除）的，且没有被占用的
        LambdaQueryWrapper<MgsBed> queryBedWrapper = new LambdaQueryWrapper<>();
        queryBedWrapper.eq(MgsBed::getStatus, CommonStatus.ACTIVE.getCode()).in(MgsBed::getRoomId, roomIds);
        List<MgsBed> bedList = mgsBedService.list(queryBedWrapper);
        //统计每个分组中元素的数量，返回 Long 类型, 将 Long 类型的统计结果转换为 Integer
        Map<Integer, Integer> bedCountMap = bedList.stream()
                .collect(Collectors.groupingBy(MgsBed::getRoomId,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
        // 构造返回结果
        roomList.stream().forEach(room -> {
            BedCountOfRoomDTO bedCountOfRoomDTO = new BedCountOfRoomDTO();
            bedCountOfRoomDTO.setRoomId(room.getId());
            bedCountOfRoomDTO.setRoomNum(room.getRoomNumber());
            bedCountOfRoomDTO.setRoomName(room.getRoomName());
            bedCountOfRoomDTO.setAvailableBedCount(bedCountMap.getOrDefault(room.getId(), 0));
            bedCountOfRoomList.add(bedCountOfRoomDTO);
        });
        return bedCountOfRoomList;
    }
}
