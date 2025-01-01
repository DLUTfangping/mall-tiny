package com.macro.mall.tiny.modules.classify.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.classify.dto.FastClassifyParam;
import com.macro.mall.tiny.modules.classify.service.FastClassifyService;
import com.macro.mall.tiny.modules.classify.vo.ClassifyPatientVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 快速分类
 * </p>
 *
 * @author macro
 * @since 2024-12-21
 */
@Api(tags = "FastClassifyController")
@Tag(name = "FastClassifyController",description = "快速分类")
@RestController
@RequestMapping("/classify/fastClassify")
public class FastClassifyController {
    @Resource
    private FastClassifyService fastClassifyService;
    @ApiOperation("快速分类")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult immediateClassify(@Validated @RequestBody FastClassifyParam param) {
        return fastClassifyService.immediateClassify(param);
    }
    @ApiOperation("根据查询条件分页获取病人列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<ClassifyPatientVO>> list(@RequestParam(value = "transferStatus", defaultValue = "1") Integer transferStatus,
                                                            @RequestParam(value = "name", required = false) String name,
                                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<ClassifyPatientVO> result = fastClassifyService.list(transferStatus, name, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(result));
    }
}

