package com.macro.mall.tiny.modules.fms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.fms.dto.UmsStudentsParam;
import com.macro.mall.tiny.modules.fms.model.UmsStudents;
import com.macro.mall.tiny.modules.fms.service.UmsStudentsService;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author macro
 * @since 2024-06-22
 */
@RestController
@Api(tags = "UmsStudentsController")
@Tag(name = "UmsStudentsController",description = "学生管理")
@RequestMapping("/students")
public class UmsStudentsController {

    @Resource
    private UmsStudentsService umsStudentsService;

    @ApiOperation("添加学生")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@Validated  @RequestBody UmsStudentsParam stu) {
        boolean success = umsStudentsService.create(stu);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据用户名分页获取用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsStudents>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<UmsStudents> adminList = umsStudentsService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(adminList));
    }

    @ApiOperation("删除学生")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteStudent(@PathVariable Integer id) {
        boolean success = umsStudentsService.deleteStudent(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("更新学生信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStudent(@RequestBody UmsStudentsParam umsStudentsParam) {
        boolean success = umsStudentsService.updateStudent(umsStudentsParam);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}

