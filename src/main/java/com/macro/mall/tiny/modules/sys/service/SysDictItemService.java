package com.macro.mall.tiny.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.sys.model.SysDictItem;

import java.util.List;

/**
 * <p>
 * 字典明细 Service
 * </p>
 *
 * @author macro
 * @since 2026-05-27
 */
public interface SysDictItemService extends IService<SysDictItem> {

    List<SysDictItem> listByDictCode(String dictCode);
}