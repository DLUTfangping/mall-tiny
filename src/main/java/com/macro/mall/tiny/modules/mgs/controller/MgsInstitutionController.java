package com.macro.mall.tiny.modules.mgs.controller;


import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.mgs.model.MgsInstitution;
import com.macro.mall.tiny.modules.mgs.service.MgsInstitutionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @author macro
 * @since 2024-12-26
 */
@Api(tags = "MgsInstitutionController")
@Tag(name = "MgsInstitutionController",description = "机构管理")
@RestController
@RequestMapping("/mgs/mgsInstitution")
public class MgsInstitutionController {

    @Resource
    private MgsInstitutionService mgsInstitutionService;

    @ApiOperation("修改机构")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody MgsInstitution param) {
        return mgsInstitutionService.updateMgsInstitution(param);
    }
}

