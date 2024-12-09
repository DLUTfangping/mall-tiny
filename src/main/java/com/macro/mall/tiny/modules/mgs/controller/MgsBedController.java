package com.macro.mall.tiny.modules.mgs.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.mgs.dto.MgsBedParam;
import com.macro.mall.tiny.modules.mgs.model.MgsBed;
import com.macro.mall.tiny.modules.mgs.service.MgsBedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 床位信息表 前端控制器
 * </p>
 *
 * @author macro
 * @since 2024-11-23
 */
@Api(tags = "MgsBedController")
@Tag(name = "MgsBedController",description = "病床管理")
@RestController
@RequestMapping("/mgs/mgsBed")
public class MgsBedController {

    @Resource
    private MgsBedService mgsBedService;

    @ApiOperation("添加病床")
    @RequestMapping(value = "/create", method = RequestMethod.POST)

    public CommonResult create(@Validated @RequestBody MgsBedParam param) {
        boolean success = mgsBedService.save(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
    @ApiOperation("删除病床")
    @RequestMapping(value = "/delete/{bedNumber}", method = RequestMethod.POST)
    public CommonResult delete(@PathVariable String bedNumber) {
        boolean success = mgsBedService.removeByBedNumber(bedNumber);
        if (success) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改病床")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody MgsBedParam param) {
        boolean success = mgsBedService.updateByBedNumber(param);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据查询条件分页获取病床列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MgsBed>> list(@RequestParam(value = "status", required = false) Integer status,
                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<MgsBed> invoiceList = mgsBedService.list(status, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(invoiceList));
    }

}

