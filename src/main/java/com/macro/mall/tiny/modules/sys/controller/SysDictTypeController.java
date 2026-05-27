package com.macro.mall.tiny.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.sys.model.SysDictType;
import com.macro.mall.tiny.modules.sys.service.SysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 字典类型 Controller
 * </p>
 *
 * @author macro
 * @since 2026-05-27
 */
@Controller
@Api(tags = "SysDictTypeController")
@Tag(name = "SysDictTypeController", description = "字典类型管理")
@RequestMapping("/dict/type")
public class SysDictTypeController {

    @Autowired
    private SysDictTypeService dictTypeService;

    @ApiOperation("获取字典类型列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<SysDictType>> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<SysDictType> page = dictTypeService.list(category, keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取字典类型详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SysDictType> getDetail(@PathVariable Long id) {
        SysDictType dictType = dictTypeService.getById(id);
        return CommonResult.success(dictType);
    }

    @ApiOperation("创建字典类型")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> create(@RequestBody SysDictType dictType) {
        dictType.setCreateTime(new Date());
        dictType.setUpdateTime(new Date());
        if (dictType.getStatus() == null) {
            dictType.setStatus(1);
        }
        boolean success = dictTypeService.save(dictType);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("创建失败");
    }

    @ApiOperation("更新字典类型")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody SysDictType dictType) {
        dictType.setId(id);
        dictType.setUpdateTime(new Date());
        boolean success = dictTypeService.updateById(dictType);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("更新失败");
    }

    @ApiOperation("删除字典类型")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = dictTypeService.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("获取所有分类")
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<java.util.List<String>> getCategories() {
        return CommonResult.success(dictTypeService.getCategories());
    }
}