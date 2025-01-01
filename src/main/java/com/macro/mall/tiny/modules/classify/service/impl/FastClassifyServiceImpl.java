package com.macro.mall.tiny.modules.classify.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.common.enums.*;
import com.macro.mall.tiny.modules.classify.dto.FastClassifyParam;
import com.macro.mall.tiny.modules.classify.service.ClassifyWristbandService;
import com.macro.mall.tiny.modules.classify.service.FastClassifyService;
import com.macro.mall.tiny.modules.classify.vo.ClassifyPatientVO;
import com.macro.mall.tiny.modules.com.dto.TransferDTO;
import com.macro.mall.tiny.modules.com.model.ComPatient;
import com.macro.mall.tiny.modules.com.model.ComPatientAdmission;
import com.macro.mall.tiny.modules.com.model.ComPatientTransfer;
import com.macro.mall.tiny.modules.com.service.ComPatientAdmissionService;
import com.macro.mall.tiny.modules.com.service.ComPatientService;
import com.macro.mall.tiny.modules.com.service.ComPatientTransferService;
import com.macro.mall.tiny.modules.com.service.CommonService;
import com.macro.mall.tiny.modules.mgs.model.MgsDepartments;
import com.macro.mall.tiny.modules.mgs.service.MgsDepartmentsService;
import com.macro.mall.tiny.modules.mgs.service.MgsRoomsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private MgsRoomsService  mgsRoomService;
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
        if (!classifyWristbandService.checkWristband(wristbandName)) return CommonResult.failed("标识号不存在");
        // 查看去向组室是否存在
        Integer toDepartmentId = param.getToDepartmentId();
        if (!mgsDepartmentsService.checkDepartment(toDepartmentId)) return CommonResult.failed("组室不存在");
        // 查看去向病房是否存在
        Integer toRoomId = param.getToRoomId();
        if (!mgsRoomService.checkRoom(toRoomId)) return CommonResult.failed("病房不存在");

        // 1.增加人员基本信息
        ComPatient comPatient = buildComPatient(param);
        comPatientService.save(comPatient);

        // 2.创建分类流转记录
        ComPatientTransfer comPatientTransfer = buildComPatientTransfer(toDepartmentId, toRoomId,
                param.getClassifyTime(), comPatient.getId());
        comPatientTransferService.save(comPatientTransfer);
        // 更新该病人其他记录中的current字段的值为0

        // 3.新增人员住院信息
        ComPatientAdmission comPatientAdmission = buildComPatientAdmission(param, comPatient.getId());
        comPatientAdmissionService.save(comPatientAdmission);
        return CommonResult.success("分类成功");
    }
    @Override
    public Page<ClassifyPatientVO> list(Integer transferStatus, String name, Integer pageSize, Integer pageNum) {
        Page<ClassifyPatientVO> voPage = new Page<>();
        // 查询当前已分类的病人列表（申请转组的病人列表）或者 查询当前已驳回的病人列表（申请转组被驳回的病人列表）
        Page<TransferDTO> transfersPage = comPatientTransferService
                        .getRequestTransfers(getMgsDepartmentId(), name, pageNum, pageSize);
        MgsDepartments departments = mgsDepartmentsService.getById(getMgsDepartmentId());
        // 构造Page<ClassifyPatientVO>
        List<ClassifyPatientVO> vos = transfersPage.getRecords().stream().map(transfer -> {
            ClassifyPatientVO vo = new ClassifyPatientVO();
            vo.setSuggestion(transfer.getAdmissionSuggestion());
            vo.setName(transfer.getPatientName());
            vo.setDepartmentName(departments.getName());
            vo.setTransferTime(transfer.getTransferTime());
            vo.setDataOriginal("");
            vo.setRejectReason(transfer.getRejectReason());
            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(vos);
        voPage.setCurrent(transfersPage.getCurrent());
        voPage.setSize(transfersPage.getSize());
        voPage.setTotal(transfersPage.getTotal());
        voPage.setPages(transfersPage.getPages());
        return voPage;
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

    private ComPatientTransfer buildComPatientTransfer(Integer toDepartmentId, Integer roomId, Date classifyTime, Integer patientId) {
        ComPatientTransfer comPatientTransfer = new ComPatientTransfer();
        // 获取病人ID号
        comPatientTransfer.setPatientId(patientId);
        comPatientTransfer.setFromDepartmentId(getMgsDepartmentId());
        comPatientTransfer.setToDepartmentId(toDepartmentId);
        comPatientTransfer.setToRoomId(roomId);
        comPatientTransfer.setTransferTime(classifyTime);
        comPatientTransfer.setTransferStatus(TransferStatusEnum.PENDING.getCode());
        comPatientTransfer.setCurrent(CurrentEnum.LATEST.getCode());
        return comPatientTransfer;
    }

    private ComPatientAdmission buildComPatientAdmission(FastClassifyParam param, Integer patientId) {
        ComPatientAdmission comPatientAdmission = new ComPatientAdmission();
        // 设置病人ID号
        comPatientAdmission.setPatientId(patientId);
        // 设置住院号
        comPatientAdmission.setHospitalNum(buildHospitalNum());
        comPatientAdmission.setWristbandName(param.getWristbandName());
        comPatientAdmission.setDepartmentId(getMgsDepartmentId());
        comPatientAdmission.setAdmissionDate(param.getClassifyTime());
        comPatientAdmission.setSuggestion(param.getSuggestion());
        comPatientAdmission.setStatus(AdmissionStatusEnum.TRANSFERRING.getCode());
        return comPatientAdmission;
    }

    private String buildPatientNum() {
        String tableName = getTableName(ComPatient.class);
        return commonService.makePatientNum(tableName);
    }

    private String buildHospitalNum() {
        return commonService.makeHospitalNum();
    }

    private Integer getMgsDepartmentId() {
        return mgsDepartmentsService.getByNameStatus(DepartmentCodeEnum.FL.getDescription(), CommonStatus.ACTIVE.getCode()).getId();
    }


}
