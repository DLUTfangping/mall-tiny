package com.macro.mall.tiny.modules.com.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.tiny.common.comutil.CommonUtil;
import com.macro.mall.tiny.modules.com.model.ComPatientAdmission;
import com.macro.mall.tiny.modules.com.service.ComPatientAdmissionService;
import com.macro.mall.tiny.modules.com.service.CommonService;
import com.macro.mall.tiny.modules.mgs.model.MgsInstitution;
import com.macro.mall.tiny.modules.mgs.service.MgsInstitutionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.com.service.impl
 * @Author: Pikachu
 * @CreateTime: 2024-12-26 21:57:41
 * @Description: 公共服务实现类
 * @Version: 1.0
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private MgsInstitutionService mgsInstitutionService;
    @Resource
    private ComPatientAdmissionService comPatientAdmissionService;
    @Override
    public String makePatientNum(String tableName) {
        String institutionNum = mgsInstitutionService.getInstitutionNum();
        String replace = tableName;
        if (StrUtil.contains(tableName, "_")) {
            replace = StrUtil.replace(tableName, "_", "");
        }
        String curtime = DateUtil.current() + "";
        String random = CommonUtil.getNewRand(4);
        return institutionNum + replace + curtime + random;
    }

    @Override
    public String makeHospitalNum() {
        // 获取机构信息
        MgsInstitution mgsInstitution = mgsInstitutionService.getMgsInstitution();

        // 获取今天的日期
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 构造 LambdaQueryWrapper
        LambdaQueryWrapper<ComPatientAdmission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(ComPatientAdmission::getCreatedAt, today + " 00:00:00")  // created_at 大于等于今天的起始时间
                .lt(ComPatientAdmission::getCreatedAt, today + " 23:59:59")  // created_at 小于今天的结束时间
                .orderByAsc(ComPatientAdmission::getId)  // 根据 id 升序排序
                .last("LIMIT 1"); // 只查询最新的一条数据

        // 获取今天最后一条数据的住院编号
        String todayNum = Optional.ofNullable(comPatientAdmissionService.getOne(queryWrapper))
                .map(ComPatientAdmission::getHospitalNum)
                .orElse("0");

        // 如果查询到编号，则截取后3位数字并转为整数，否则默认为0
        int todayNumInt = todayNum.equals("0") ? 0 : Integer.parseInt(todayNum.substring(todayNum.length() - 3));

        // 获取今天的日期部分
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 生成新的住院编号
        String todayOrder = String.format("%04d", todayNumInt + 1);

        // 返回完整的住院编号
        return mgsInstitution.getAreaNumber() + mgsInstitution.getInstitutionCode() + date + todayOrder;
    }

}
