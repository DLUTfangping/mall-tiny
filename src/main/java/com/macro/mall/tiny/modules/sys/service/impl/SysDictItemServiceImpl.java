package com.macro.mall.tiny.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.tiny.modules.sys.mapper.SysDictItemMapper;
import com.macro.mall.tiny.modules.sys.model.SysDictItem;
import com.macro.mall.tiny.modules.sys.service.SysDictItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典明细 Service 实现
 * </p>
 *
 * @author macro
 * @since 2026-05-27
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

    @Override
    public List<SysDictItem> listByDictCode(String dictCode) {
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        if (dictCode != null && !dictCode.isEmpty()) {
            wrapper.eq(SysDictItem::getDictCode, dictCode);
        }
        wrapper.orderByAsc(SysDictItem::getItemSort, SysDictItem::getCreateTime);
        return this.list(wrapper);
    }
}