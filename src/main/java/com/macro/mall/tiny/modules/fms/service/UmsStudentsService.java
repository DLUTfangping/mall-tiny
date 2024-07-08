package com.macro.mall.tiny.modules.fms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.fms.dto.UmsStudentsParam;
import com.macro.mall.tiny.modules.fms.model.UmsStudents;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.ums.model.UmsRole;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author macro
 * @since 2024-06-22
 */
public interface UmsStudentsService extends IService<UmsStudents> {

    boolean create(UmsStudentsParam umsStudentsParam);

    Page<UmsStudents> list(String userName, Integer pageSize, Integer pageNum);

    boolean deleteStudent(Integer id);

    boolean updateStudent(UmsStudentsParam umsStudentsParam);
}
