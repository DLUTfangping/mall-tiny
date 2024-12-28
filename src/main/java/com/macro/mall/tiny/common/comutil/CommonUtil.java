package com.macro.mall.tiny.common.comutil;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

import java.util.Random;

/**
 * @BelongsProject: mall-tiny
 * @BelongsPackage: com.macro.mall.tiny.common.comutil
 * @Author: Pikachu
 * @CreateTime: 2024-12-26 22:04:23
 * @Description: 公共工具类
 * @Version: 1.0
 */
public class CommonUtil {
    public static String getTableName(Class<?> entityClass) {
        // 获取实体类对应的 TableInfo
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        if (tableInfo != null) {
            return tableInfo.getTableName();
        }
        throw new RuntimeException("未找到对应的表名，请检查实体类是否配置了 @TableName 注解");
    }

    /**
     * @Description: 生成随机四位字符串
     * @Author: Pikachu
     * @date: 2024/12/26 10:05 PM
     * @param: [len]
     * @return: java.lang.String
     **/
    public static String getNewRand(int len) {
        // 字符数组
        char[] charsArray = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z'
        };

        Random random = new Random();
        StringBuilder outputStr = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(charsArray.length); // 随机索引
            outputStr.append(charsArray[randomIndex]);          // 添加随机字符
        }

        return outputStr.toString();
    }
}
