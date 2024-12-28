package com.macro.mall.tiny.modules.classify.controller;


import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.classify.dto.FastClassifyParam;
import com.macro.mall.tiny.modules.classify.service.FastClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
//    @ApiOperation("删除标识")
//    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//    public CommonResult delete(@PathVariable Integer id) {
//        boolean success = classifyWristbandService.removeById(id);
//        if (success) {
//            return CommonResult.success(null);
//        } else {
//            return CommonResult.failed();
//        }
//    }
//
//    @ApiOperation("修改标识")
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public CommonResult update(@RequestBody ClassifyWristband param) {
//        param.setUpdatedAt(new Date());
//        boolean success = classifyWristbandService.updateById(param);
//        if (success) {
//            return CommonResult.success(null);
//        }
//        return CommonResult.failed();
//    }
//
//    @ApiOperation("根据查询条件分页获取标识列表")
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public CommonResult<CommonPage<ClassifyWristband>> list(@RequestParam(value = "status", required = false) Integer status,
//                                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
//                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
//                                                         @RequestParam(value = "name", required = false) String name,
//                                                         @RequestParam(value = "fixedCode", required = false) String fixedCode,
//                                                         @RequestParam(value = "boundPersonNum", required = false) String boundPersonNum) {
//        Page<ClassifyWristband> invoiceList = classifyWristbandService.list(status, pageSize, pageNum, name, fixedCode, boundPersonNum);
//        return CommonResult.success(CommonPage.restPage(invoiceList));
//    }
}

