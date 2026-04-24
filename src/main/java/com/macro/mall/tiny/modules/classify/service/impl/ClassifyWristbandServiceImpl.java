package com.macro.mall.tiny.modules.classify.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.enums.BoundedEnum;
import com.macro.mall.tiny.common.enums.CommonStatus;
import com.macro.mall.tiny.modules.classify.mapper.ClassifyWristbandMapper;
import com.macro.mall.tiny.modules.classify.model.ClassifyWristband;
import com.macro.mall.tiny.modules.classify.service.ClassifyWristbandService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标识管理表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-12-21
 */
@Service
public class ClassifyWristbandServiceImpl extends ServiceImpl<ClassifyWristbandMapper, ClassifyWristband>
        implements ClassifyWristbandService {


    @Override
    public Page<ClassifyWristband> list(Integer status, Integer pageSize, Integer pageNum,
                                     String name, String fixedCode, String boundPersonNum) {
        QueryWrapper<ClassifyWristband> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ClassifyWristband> lambda = wrapper.lambda();
        if (status != null) {
            lambda.eq(ClassifyWristband::getStatus, status);
        }
        if (StrUtil.isNotBlank(name)) {
            lambda.like(ClassifyWristband::getName, name);
        }
        if (StrUtil.isNotBlank(fixedCode)) {
            lambda.like(ClassifyWristband::getFixedCode, fixedCode);
        }
        if (StrUtil.isNotBlank(boundPersonNum)) {
            lambda.eq(ClassifyWristband::getBoundPersonNum, boundPersonNum);
        }
        Page<ClassifyWristband> page = new Page<>(pageNum,pageSize);
        return page(page, wrapper);
    }

    @Override
    public boolean checkWristband(String wristbandName) {
        LambdaQueryWrapper<ClassifyWristband> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClassifyWristband::getName, wristbandName)
                .eq(ClassifyWristband::getStatus, CommonStatus.ACTIVE.getCode())
                .eq(ClassifyWristband::getBounded, BoundedEnum.UNBOUND.getCode());
        if (count(queryWrapper) > 0) {
            return true;
        }
        return false;
    }
}
