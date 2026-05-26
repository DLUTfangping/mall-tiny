# medicine-menu-generator

生成药材保障模块的菜单结构（一级菜单 + 二级菜单），包括：
- 后端 Controller/Service/Mapper/Model 文件生成
- 前端 Vue 页面/API/路由配置
- 数据库菜单 SQL

## Usage

```bash
/skill medicine-menu-generator
```

## Input Parameters

生成菜单时需要提供以下信息：

| 参数 | 说明 | 示例 |
|------|------|------|
| 一级菜单名称 | 顶级菜单名称 | 药材保障 |
| 一级菜单标识 | 英文标识，用于 name 字段 | medicine |
| 一级菜单图标 | 侧边栏图标 | product |
| 二级菜单名称 | 子菜单名称 | 药材字典 |
| 二级菜单标识 | 英文标识，用于 name 字段 | medicineDrug |
| 二级菜单路由 | 前端路由路径 | drug |
| 功能模块名 | 功能英文名，用于文件命名 | Drug |
| 功能描述 | 中文功能描述 | 药材字典管理 |

## Output Files

### 后端 (mall-tiny)

| 文件 | 说明 |
|------|------|
| `modules/medicine/model/Medicine{Module}.java` | 实体类 |
| `modules/medicine/mapper/Medicine{Module}Mapper.java` | Mapper 接口 |
| `modules/medicine/service/Medicine{Module}Service.java` | Service 接口 |
| `modules/medicine/service/impl/Medicine{Module}ServiceImpl.java` | Service 实现 |
| `modules/medicine/controller/Medicine{Module}Controller.java` | Controller |

### 前端 (mall-admin-web)

| 文件 | 说明 |
|------|------|
| `src/api/medicine/{module}.js` | API 调用 |
| `src/views/medicine/{module}/index.vue` | 列表页面 |
| `src/router/index.js` | 路由配置（需手动添加） |

### 数据库

| 文件 | 说明 |
|------|------|
| SQL INSERT 语句 | 插入 ums_menu 表 |

## 代码模板

### 后端 Model

```java
package com.macro.mall.tiny.modules.medicine.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * {功能描述}实体
 * </p>
 *
 * @author macro
 * @since {日期}
 */
@Data
@TableName("medicine_{module}")
@ApiModel(value = "Medicine{Module}对象", description = "{功能描述}")
public class Medicine{Module} implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // TODO: 根据实际需求添加字段

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
```

### 后端 Mapper

```java
package com.macro.mall.tiny.modules.medicine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macro.mall.tiny.modules.medicine.model.Medicine{Module};
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * {功能描述} Mapper 接口
 * </p>
 *
 * @author macro
 * @since {日期}
 */
@Mapper
public interface Medicine{Module}Mapper extends BaseMapper<Medicine{Module}> {

}
```

### 后端 Service 接口

```java
package com.macro.mall.tiny.modules.medicine.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.macro.mall.tiny.modules.medicine.model.Medicine{Module};

/**
 * <p>
 * {功能描述} Service 接口
 * </p>
 *
 * @author macro
 * @since {日期}
 */
public interface Medicine{Module}Service extends IService<Medicine{Module}> {

    /**
     * 分页查询列表
     */
    Page<Medicine{Module}> list(String keyword, Integer pageSize, Integer pageNum);
}
```

### 后端 Service 实现

```java
package com.macro.mall.tiny.modules.medicine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macro.mall.tiny.modules.medicine.mapper.Medicine{Module}Mapper;
import com.macro.mall.tiny.modules.medicine.model.Medicine{Module};
import com.macro.mall.tiny.modules.medicine.service.Medicine{Module}Service;
import org.springframework.stereotype.Service;

/**
 * <p>
 * {功能描述} Service 实现
 * </p>
 *
 * @author macro
 * @since {日期}
 */
@Service
public class Medicine{Module}ServiceImpl extends ServiceImpl<Medicine{Module}Mapper, Medicine{Module}> implements Medicine{Module}Service {

    @Override
    public Page<Medicine{Module}> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<Medicine{Module}> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Medicine{Module}> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("name", keyword);
        }
        wrapper.orderByDesc("create_time");
        return this.page(page, wrapper);
    }
}
```

### 后端 Controller

```java
package com.macro.mall.tiny.modules.medicine.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.medicine.model.Medicine{Module};
import com.macro.mall.tiny.modules.medicine.service.Medicine{Module}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * {功能描述} Controller
 * </p>
 *
 * @author macro
 * @since {日期}
 */
@Controller
@Api(tags = "Medicine{Module}Controller")
@Tag(name = "Medicine{Module}Controller", description = "{功能描述}")
@RequestMapping("/{module}")
public class Medicine{Module}Controller {

    @Autowired
    private Medicine{Module}Service {module}Service;

    @ApiOperation("获取列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<Medicine{Module}>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<Medicine{Module}> page = {module}Service.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("获取详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Medicine{Module}> getDetail(@PathVariable Long id) {
        Medicine{Module} item = {module}Service.getById(id);
        return CommonResult.success(item);
    }

    @ApiOperation("创建")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> create(@RequestBody Medicine{Module} item) {
        boolean success = {module}Service.save(item);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("更新")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> update(@PathVariable Long id, @RequestBody Medicine{Module} item) {
        item.setId(id);
        boolean success = {module}Service.updateById(item);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> delete(@PathVariable Long id) {
        boolean success = {module}Service.removeById(id);
        if (success) {
            return CommonResult.success(null);
        }
        return CommonResult.failed();
    }
}
```

### 前端 API

```javascript
import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/{module}/list',
    method: 'get',
    params
  })
}

export function getItem(id) {
  return request({
    url: `/{module}/${id}`,
    method: 'get'
  })
}

export function create(data) {
  return request({
    url: '/{module}/create',
    method: 'post',
    data
  })
}

export function update(id, data) {
  return request({
    url: `/{module}/update/${id}`,
    method: 'post',
    data
  })
}

export function deleteItem(id) {
  return request({
    url: `/{module}/delete/${id}`,
    method: 'post'
  })
}
```

### 前端 Vue 页面模板

```vue
<template>
  <div class="{module}-management">
    <div class="content-header">
      <h3 class="content-title">{功能名称}</h3>
    </div>
    <div class="filter-container">
      <el-input v-model="listQuery.keyword" placeholder="关键词" style="width: 200px;" clearable />
      <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="listLoading" :data="list" border stripe style="width: 100%">
      <el-table-column label="ID" prop="id" width="80" align="center" />
      <el-table-column label="创建时间" width="180" align="center">
        <template slot-scope="scope">{{ formatDate(scope.row.createTime) }}</template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script>
import { list } from '@/api/medicine/{module}'

export default {
  name: 'Medicine{Module}',
  data() {
    return {
      list: [],
      listLoading: false,
      total: 0,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        keyword: ''
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      list(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleSearch() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = { pageNum: 1, pageSize: 10, keyword: '' }
      this.getList()
    },
    handlePageChange(page) {
      this.listQuery.pageNum = page
      this.getList()
    },
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
    }
  }
}
</script>

<style scoped>
.{module}-management { padding: 20px; }
.content-header { margin-bottom: 20px; }
.content-title { margin: 0; font-size: 18px; font-weight: 500; }
.filter-container { margin-bottom: 20px; display: flex; gap: 10px; flex-wrap: wrap; }
</style>
```

## 添加一级菜单（新建父菜单 + 二级菜单）

### SQL

```sql
-- 一级菜单：{一级菜单名称}
INSERT INTO ums_menu (parent_id, create_time, title, level, sort, name, icon, hidden)
VALUES (0, NOW(), '{一级菜单名称}', 0, 0, '{parentName}', '{icon}', 0);

-- 二级菜单：{二级菜单名称}
INSERT INTO ums_menu (parent_id, create_time, title, level, sort, name, icon, hidden)
VALUES (LAST_INSERT_ID(), NOW(), '{二级菜单名称}', 1, 0, '{name}', 'product-list', 0);
```

### 路由配置

在 `src/router/index.js` 的 `asyncRouterMap` 中添加：

```javascript
{
  path: '/{parentRoute}',
  component: Layout,
  redirect: '/{parentRoute}/{route}',
  name: '{parentRoute}',
  meta: {title: '{一级菜单标题}', icon: '{icon}'},
  children: [{
    path: '{route}',
    name: '{name}',
    component: () => import('@/views/{parentRoute}/{route}/index'),
    meta: {title: '{二级菜单标题}', icon: 'product-list'}
  }]
}
```

## 添加二级菜单（到已有父菜单）

### SQL

```sql
-- 查找一级菜单ID
SELECT id, name, title FROM ums_menu WHERE name = '{parentName}' AND level = 0;

-- 添加二级菜单（假设一级菜单ID为 {parentId}）
INSERT INTO ums_menu (parent_id, create_time, title, level, sort, name, icon, hidden)
VALUES ({parentId}, NOW(), '{二级菜单名称}', 1, {sort}, '{name}', 'product-list', 0);
```

### 路由配置

在父菜单的 `children` 数组中添加：

```javascript
{
  path: '{route}',
  name: '{name}',
  component: () => import('@/views/{parentRoute}/{route}/index'),
  meta: {title: '{二级菜单标题}', icon: 'product-list'}
}
```

## 完整示例

生成"药材字典"菜单：

**输入：**
- 一级菜单名称：药材保障
- 一级菜单标识：medicine
- 一级菜单图标：product
- 二级菜单名称：药材字典
- 二级菜单标识：medicineDrug
- 二级菜单路由：drug
- 功能模块名：Drug
- 功能描述：药材字典管理

**后端文件：**
- `modules/medicine/model/MedicineDrug.java`
- `modules/medicine/mapper/MedicineDrugMapper.java`
- `modules/medicine/service/MedicineDrugService.java`
- `modules/medicine/service/impl/MedicineDrugServiceImpl.java`
- `modules/medicine/controller/MedicineDrugController.java`

**前端文件：**
- `src/api/medicine/drug.js`
- `src/views/medicine/drug/index.vue`

**路由：**
```javascript
{
  path: 'drug',
  name: 'medicineDrug',
  component: () => import('@/views/medicine/drug/index'),
  meta: {title: '药材字典', icon: 'product-list'}
}
```

**SQL：**
```sql
INSERT INTO ums_menu (parent_id, create_time, title, level, sort, name, icon, hidden)
VALUES (LAST_INSERT_ID(), NOW(), '药材字典', 1, 0, 'medicineDrug', 'product-list', 0);
```
（假设 LAST_INSERT_ID() 返回药材保障的ID）

## 扩展定制

根据实际业务需求，可扩展以下内容：

### Model 字段示例

```java
@ApiModelProperty(value = "名称")
private String name;

@ApiModelProperty(value = "编码")
private String code;

@ApiModelProperty(value = "状态")
private Integer status;

@ApiModelProperty(value = "备注")
private String remark;
```

### Controller 扩展接口示例

```java
@ApiOperation("修改状态")
@RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
@ResponseBody
public CommonResult<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
    // 实现逻辑
}
```

### 前端筛选条件示例

```javascript
listQuery: {
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  status: null,
  category: ''
}
```