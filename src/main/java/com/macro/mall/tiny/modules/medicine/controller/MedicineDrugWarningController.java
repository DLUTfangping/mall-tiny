package com.macro.mall.tiny.modules.medicine.controller;

import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.medicine.model.MedicineDrugWarning;
import com.macro.mall.tiny.modules.medicine.service.MedicineDrugWarningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@Api(tags = "MedicineDrugWarningController")
@Tag(name = "MedicineDrugWarningController", description = "药品预警管理")
@RequestMapping("/drugWarning")
public class MedicineDrugWarningController {

    @Autowired
    private MedicineDrugWarningService drugWarningService;

    @ApiOperation("获取药品预警详情")
    @RequestMapping(value = "/{drugCode}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<MedicineDrugWarning> getByDrugCode(@PathVariable String drugCode) {
        MedicineDrugWarning warning = drugWarningService.lambdaQuery()
                .eq(MedicineDrugWarning::getDrugCode, drugCode)
                .one();
        return CommonResult.success(warning);
    }

    @ApiOperation("创建或更新药品预警")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> save(@RequestBody MedicineDrugWarning warning) {
        warning.setUpdateTime(new Date());
        if (warning.getId() == null) {
            warning.setCreateTime(new Date());
        }
        boolean success = drugWarningService.saveOrUpdate(warning);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
