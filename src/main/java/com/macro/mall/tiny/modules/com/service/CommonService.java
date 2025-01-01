package com.macro.mall.tiny.modules.com.service;

public interface CommonService {
    /**
     * @Description: 生成病人编号
     * @Author: Pikachu
     * @date: 2024/12/28 4:36 PM
     * @param: [tableName]
     * @return: java.lang.String
     **/
    String makePatientNum(String tableName);

    /**
     * @Description: 生成病例号/住院号
     * @Author: Pikachu
     * @date: 2024/12/28 4:36 PM
     * @param: [tableName]
     * @return: java.lang.String
     **/
    String makeHospitalNum();

    /**
     * @Description: 获取已分类/未分类/已驳回病人列表
     * @Author: Pikachu
     * @date: 2024/12/28 4:36 PM
     * @param: [transferStatus, departmentId, name, pageSize, pageNum]
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.macro.mall.tiny.modules.classify.model.ClassifyWristband>
     **/
//    Page<ClassifyPatientVO> list(Integer transferStatus, Integer departmentId, String name, Integer pageSize, Integer pageNum);
}
