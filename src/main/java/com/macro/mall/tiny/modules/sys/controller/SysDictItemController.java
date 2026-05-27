package com.macro.mall.tiny.modules.sys.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.sys.model.SysDictItem;
import com.macro.mall.tiny.modules.sys.service.SysDictItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 字典明细 Controller
 * </p>
 *
 * @author macro
 * @since 2026-05-27
 */
@Controller
@Api(tags = "SysDictItemController")
@Tag(name = "SysDictItemController", description = "字典明细管理")
@RequestMapping("/dict/item")
public class SysDictItemController {

    @Autowired
    private SysDictItemService dictItemService;

    @ApiOperation("获取字典明细列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SysDictItem>> list(
            @RequestParam(required = false) String dictCode) {
        List<SysDictItem> list = dictItemService.listByDictCode(dictCode);
        return CommonResult.success(list);
    }

    @ApiOperation("获取字典明细详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SysDictItem> getDetail(@PathVariable Long id) {
        SysDictItem item = dictItemService.getById(id);
        return CommonResult.success(item);
    }

    @ApiOperation("创建字典明细")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> create(@RequestBody SysDictItem item) {
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        if (item.getStatus() == null) {
            item.setStatus(1);
        }
        boolean success = dictItemService.save(item);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @ApiOperation("更新字典明细")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody SysDictItem item) {
        item.setId(id);
        item.setUpdateTime(new Date());
        boolean success = dictItemService.updateById(item);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @ApiOperation("删除字典明细")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = dictItemService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }
}