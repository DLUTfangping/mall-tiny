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

}
