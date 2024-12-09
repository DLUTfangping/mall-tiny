package com.macro.mall.tiny.modules.mgs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.mgs.dto.MgsBedParam;
import com.macro.mall.tiny.modules.mgs.mapper.MgsBedMapper;
import com.macro.mall.tiny.modules.mgs.model.MgsBed;
import com.macro.mall.tiny.modules.mgs.service.MgsBedService;
import org.springframework.stereotype.Service;

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
    @Override
    public boolean save(MgsBedParam param) {
        MgsBed mgsBed = new MgsBed();
        BeanUtil.copyProperties(param, mgsBed);
        return save(mgsBed);
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
    public Page<MgsBed> list(Integer status, Integer pageSize, Integer pageNum) {
        QueryWrapper<MgsBed> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MgsBed> lambda = wrapper.lambda();
        lambda.eq(MgsBed::getStatus, status);
        Page<MgsBed> page = new Page<>(pageNum,pageSize);
        return page(page, wrapper);
    }
}
