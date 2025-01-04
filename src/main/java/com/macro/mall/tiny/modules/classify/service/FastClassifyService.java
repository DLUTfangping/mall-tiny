package com.macro.mall.tiny.modules.classify.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.classify.dto.FastClassifyParam;
import com.macro.mall.tiny.modules.com.dto.ClassifyTransferDTO;

public interface FastClassifyService {
    /*
     * @Description:立即分类
     * @Author: Pikachu
     * @date: 2024/12/25 10:12 PM
     * @param: [param]
     * @return: com.macro.mall.tiny.common.api.CommonResult
     **/
    CommonResult immediateClassify(FastClassifyParam param);


    /*
     * @Description: 查询已分类病人、已驳回病人列表
     * @Author: Pikachu
     * @date: 2024/12/30 6:59 AM
     * @param: [transferStatus 待接收 0 已驳回 2, name 病人姓名, pageSize, pageNum]
     * @return: com.macro.mall.tiny.common.api.CommonResult
     **/
    Page<ClassifyTransferDTO> list(Integer transferStatus, String name, Integer pageSize, Integer pageNum);
}
