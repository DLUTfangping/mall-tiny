package com.macro.mall.tiny.modules.mgs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.mgs.model.MgsInstitution;

/**
 * <p>
 * 机构信息表 服务类
 * </p>
 *
 * @author macro
 * @since 2024-12-26
 */
public interface MgsInstitutionService extends IService<MgsInstitution> {

    CommonResult updateMgsInstitution(MgsInstitution mgsInstitution);

    String getInstitutionNum();

    MgsInstitution getMgsInstitution();
}
