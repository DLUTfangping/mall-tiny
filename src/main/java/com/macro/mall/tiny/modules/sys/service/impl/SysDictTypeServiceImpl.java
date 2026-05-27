package com.macro.mall.tiny.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.modules.sys.mapper.SysDictTypeMapper;
import com.macro.mall.tiny.modules.sys.model.SysDictType;
import com.macro.mall.tiny.modules.sys.service.SysDictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典类型 Service 实现
 * </p>
 *
 * @author macro
 * @since 2026-05-27
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Override
    public Page<SysDictType> list(String category, String keyword, Integer pageSize, Integer pageNum) {
        Page<SysDictType> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        if (category != null && !category.isEmpty()) {
            wrapper.eq(SysDictType::getCategory, category);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysDictType::getDictCode, keyword)
                    .or().like(SysDictType::getDictName, keyword));
        }
        wrapper.orderByDesc(SysDictType::getSort, SysDictType::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public List<String> getCategories() {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysDictType::getCategory)
                .isNotNull(SysDictType::getCategory)
                .ne(SysDictType::getCategory, "")
                .groupBy(SysDictType::getCategory);
        List<SysDictType> list = this.list(wrapper);
        return list.stream()
                .map(SysDictType::getCategory)
                .filter(c -> c != null && !c.isEmpty())
                .collect(Collectors.toList());
    }
}