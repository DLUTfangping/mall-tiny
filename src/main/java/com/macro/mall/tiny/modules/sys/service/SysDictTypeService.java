package com.macro.mall.tiny.modules.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.sys.model.SysDictType;

import java.util.List;

/**
 * <p>
 * 字典类型 Service
 * </p>
 *
 * @author macro
 * @since 2026-05-27
 */
public interface SysDictTypeService extends IService<SysDictType> {

    Page<SysDictType> list(String category, String keyword, Integer pageSize, Integer pageNum);

    List<String> getCategories();
}