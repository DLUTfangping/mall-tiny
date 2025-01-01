package com.macro.mall.tiny.modules.classify.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.classify.model.ClassifyWristband;

/**
 * <p>
 * 标识管理表 服务类
 * </p>
 *
 * @author macro
 * @since 2024-12-21
 */
public interface ClassifyWristbandService extends IService<ClassifyWristband> {
    Page<ClassifyWristband> list(Integer status, Integer pageSize, Integer pageNum,
                              String name, String fixedCode, String boundPersonNum);

    boolean checkWristband(String wristbandName);
}
