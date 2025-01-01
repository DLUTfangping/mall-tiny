package com.macro.mall.tiny.modules.mgs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.mgs.dto.BedCountOfRoomDTO;
import com.macro.mall.tiny.modules.mgs.dto.MgsDepartmentsParam;
import com.macro.mall.tiny.modules.mgs.model.MgsDepartments;

import java.util.List;

/**
 * <p>
 * 组室信息表 服务类
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
public interface MgsDepartmentsService extends IService<MgsDepartments> {
    boolean save(MgsDepartmentsParam param);

    boolean removeById(Integer id);

    boolean updateById(MgsDepartmentsParam param);

    Page<MgsDepartments> list(Integer status, Integer pageSize, Integer pageNum);

    MgsDepartments getByNameStatus(String name, Integer status);

    boolean checkDepartment(Integer departmentId);

    List<BedCountOfRoomDTO> getBedCountOfRoomList(Integer departmentId);

}
