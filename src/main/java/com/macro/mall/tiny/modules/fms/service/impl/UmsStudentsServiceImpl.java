package com.macro.mall.tiny.modules.fms.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.fms.dto.UmsStudentsParam;
import com.macro.mall.tiny.modules.fms.model.UmsStudents;
import com.macro.mall.tiny.modules.fms.mapper.UmsStudentsMapper;
import com.macro.mall.tiny.modules.fms.service.UmsStudentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-06-22
 */
@Service
public class UmsStudentsServiceImpl extends ServiceImpl<UmsStudentsMapper, UmsStudents> implements UmsStudentsService {

    @Override
    public boolean create(UmsStudentsParam param) {

        UmsStudents students = new UmsStudents();
        BeanUtils.copyProperties(param, students);
        save(students);
        return true;
    }

    @Override
    public Page<UmsStudents> list(String userName, Integer pageSize, Integer pageNum) {
        Page<UmsStudents> page = new Page<>(pageNum,pageSize);
        QueryWrapper<UmsStudents> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<UmsStudents> lambda = wrapper.lambda();
        if(StrUtil.isNotEmpty(userName)){
            lambda.like(UmsStudents::getName,userName);
            lambda.or().like(UmsStudents::getName,userName);
        }
        return page(page,wrapper);
    }

    @Override
    public boolean deleteStudent(Integer id) {
        // 根据id查询学生信息，判断是否存在
        UmsStudents umsStudent = getById(id);
        if (umsStudent == null) return false;
        // 删除学生信息，也就是将学生的状态信息置为1
        UpdateWrapper<UmsStudents> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("status", UmsStudents.OFF_STATE);
        return update(null, updateWrapper);
    }

    @Override
    public boolean updateStudent(UmsStudentsParam umsStudentsParam) {
        if (umsStudentsParam == null) return false;
        UmsStudents umsStudents = new UmsStudents();
        BeanUtils.copyProperties(umsStudentsParam, umsStudents);
        return updateById(umsStudents);
    }
}
