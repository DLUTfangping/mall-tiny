package com.macro.mall.tiny.modules.mgs.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.mgs.mapper.MgsInstitutionMapper;
import com.macro.mall.tiny.modules.mgs.model.MgsInstitution;
import com.macro.mall.tiny.modules.mgs.service.MgsInstitutionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 机构信息表 服务实现类
 * </p>
 *
 * @author macro
 * @since 2024-12-26
 */
@Service
public class MgsInstitutionServiceImpl extends ServiceImpl<MgsInstitutionMapper, MgsInstitution> implements MgsInstitutionService {
    @Override
    public CommonResult updateMgsInstitution(MgsInstitution mgsInstitution) {
        if (mgsInstitution == null) return CommonResult.failed();
        UpdateWrapper<MgsInstitution> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(MgsInstitution::getInstitutionNum, "AAA" + mgsInstitution.getAreaNumber())
                .set(MgsInstitution::getAreaNumber, mgsInstitution.getAreaNumber())
                .set(MgsInstitution::getInstitutionCode, mgsInstitution.getInstitutionCode())
                .set(MgsInstitution::getName, mgsInstitution.getName())
                .set(MgsInstitution::getSuperiorAddress, mgsInstitution.getSuperiorAddress())
                .set(MgsInstitution::getSuperiorName, mgsInstitution.getSuperiorName())
                .set(MgsInstitution::getSuperiorNum, mgsInstitution.getSuperiorNum())
                .set(MgsInstitution::getSuperiorCode, mgsInstitution.getSuperiorCode());

        boolean update = update(updateWrapper);
        if (!update) return CommonResult.failed("更新失败");
        return CommonResult.success(null);
    }

    @Override
    public String getInstitutionNum() {
        List<MgsInstitution> list = list();
        MgsInstitution mgsInstitution = list.get(0);
        return mgsInstitution.getInstitutionNum();
    }


    @Override
    public MgsInstitution getMgsInstitution() {
        List<MgsInstitution> list = list();
        return list.get(0);
    }


}
