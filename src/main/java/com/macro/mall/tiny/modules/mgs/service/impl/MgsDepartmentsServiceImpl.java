package com.macro.mall.tiny.modules.mgs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.mgs.dto.MgsDepartmentsParam;
import com.macro.mall.tiny.modules.mgs.mapper.MgsDepartmentsMapper;
import com.macro.mall.tiny.modules.mgs.model.MgsDepartments;
import com.macro.mall.tiny.modules.mgs.service.MgsDepartmentsService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 组室信息表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Service
public class MgsDepartmentsServiceImpl extends ServiceImpl<MgsDepartmentsMapper, MgsDepartments> implements MgsDepartmentsService {

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
}
