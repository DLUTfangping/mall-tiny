package com.macro.mall.tiny.modules.classify.service;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.classify.dto.FastClassifyParam;

public interface FastClassifyService {
    /*
     * @Description:立即分类
     * @Author: Pikachu
     * @date: 2024/12/25 10:12 PM
     * @param: [param]
     * @return: com.macro.mall.tiny.common.api.CommonResult
     **/
    CommonResult immediateClassify(FastClassifyParam param);
}
