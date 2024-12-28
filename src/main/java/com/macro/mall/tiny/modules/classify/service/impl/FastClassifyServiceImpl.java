package com.macro.mall.tiny.modules.classify.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.enums.AdmissionStatusEnum;
import com.macro.mall.tiny.common.enums.GenderEnum;
import com.macro.mall.tiny.common.enums.TransferStatusEnum;
import com.macro.mall.tiny.modules.classify.dto.FastClassifyParam;
import com.macro.mall.tiny.modules.classify.model.ClassifyWristband;
import com.macro.mall.tiny.modules.classify.service.ClassifyWristbandService;
import com.macro.mall.tiny.modules.classify.service.FastClassifyService;
import com.macro.mall.tiny.modules.com.model.ComPatient;
import com.macro.mall.tiny.modules.com.model.ComPatientAdmission;
import com.macro.mall.tiny.modules.com.model.ComPatientTransfer;
import com.macro.mall.tiny.modules.com.service.ComPatientAdmissionService;
import com.macro.mall.tiny.modules.com.service.ComPatientService;
import com.macro.mall.tiny.modules.com.service.ComPatientTransferService;
import com.macro.mall.tiny.modules.com.service.CommonService;
import com.macro.mall.tiny.modules.mgs.service.MgsDepartmentsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.macro.mall.tiny.common.comutil.CommonUtil.getTableName;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.modules.classify.service.impl
 * @Author: Pikachu
 * @CreateTime: 2024-12-25 22:10:08
 * @Description: 快速分类接口实现类
 * @Version: 1.0
 */
@Service
public class FastClassifyServiceImpl implements FastClassifyService {

    @Resource
    private ClassifyWristbandService classifyWristbandService;
    @Resource
    private MgsDepartmentsService mgsDepartmentsService;
    @Resource
    private ComPatientService comPatientService;
    @Resource
    private ComPatientTransferService comPatientTransferService;
    @Resource
    private ComPatientAdmissionService comPatientAdmissionService;

    @Resource
    private CommonService commonService;
    @Transactional
    @Override
    public CommonResult immediateClassify(FastClassifyParam param) {
        // 查看标识号是否存在
        String wristbandName = param.getWristbandName();
        if (!checkWristband(wristbandName)) return CommonResult.failed("标识号不存在");
        // 查看去向组室是否存在
        Integer toDepartmentId = param.getToDepartmentId();
        if (!checkDepartment(toDepartmentId)) return CommonResult.failed("组室不存在");

        // 1.增加人员基本信息
        ComPatient comPatient = buildComPatient(param);
        comPatientService.save(comPatient);

        // 2.创建分类流转记录
        ComPatientTransfer comPatientTransfer = buildComPatientTransfer(toDepartmentId, comPatient.getId());
        comPatientTransferService.save(comPatientTransfer);

        // 3.新增人员住院信息
        ComPatientAdmission comPatientAdmission = buildComPatientAdmission(param, comPatient.getId());
        comPatientAdmissionService.save(comPatientAdmission);
        return CommonResult.success("分类成功");
    }


    private boolean checkWristband(String wristbandName) {
        LambdaQueryWrapper<ClassifyWristband> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClassifyWristband::getName, wristbandName);
        return classifyWristbandService.count(queryWrapper) > 0;
    }

    private boolean checkDepartment(Integer departmentId) {
        return mgsDepartmentsService.getById(departmentId) != null;
    }

    private ComPatient buildComPatient(FastClassifyParam param) {
        ComPatient comPatient = new ComPatient();
        comPatient.setSignage(StrUtil.isBlank(param.getSignage()) ? "" : param.getSignage());
        // 生成病人编号
        comPatient.setPatientNum(buildPatientNum());
        comPatient.setName(StrUtil.isBlank(param.getName()) ? "" : param.getName());
        comPatient.setGender(GenderEnum.UNDEFINED.getCode());
        return comPatient;
    }

    private ComPatientTransfer buildComPatientTransfer(Integer toDepartmentId, Integer patientId) {
        ComPatientTransfer comPatientTransfer = new ComPatientTransfer();
        // 获取病人ID号
        comPatientTransfer.setPatientId(patientId);
        comPatientTransfer.setFromDepartmentId(-1);
        comPatientTransfer.setToDepartmentId(toDepartmentId);
        comPatientTransfer.setReceiveTime(DateUtil.date());
        comPatientTransfer.setTransferStatus(TransferStatusEnum.RECEIVED.getCode());
        return comPatientTransfer;
    }

    private ComPatientAdmission buildComPatientAdmission(FastClassifyParam param, Integer patientId) {
        ComPatientAdmission comPatientAdmission = new ComPatientAdmission();
        // 获取病人ID号
        comPatientAdmission.setPatientId(patientId);
        // 获取住院号
        comPatientAdmission.setHospitalNum(buildHospitalNum());
        comPatientAdmission.setWristbandName(param.getWristbandName());
        comPatientAdmission.setDepartmentId(param.getToDepartmentId());
        comPatientAdmission.setAdmissionDate(DateUtil.date());
        comPatientAdmission.setSuggestion(param.getSuggestion());
        comPatientAdmission.setStatus(AdmissionStatusEnum.IN_HOSPITAL.getCode());
        return comPatientAdmission;
    }

    private String buildPatientNum() {
        String tableName = getTableName(ComPatient.class);
        return commonService.makePatientNum(tableName);
    }

    private String buildHospitalNum() {
        return commonService.makeHospitalNum();
    }
}
