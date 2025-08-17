/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.config.event.execl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.context.ContextUtils;
import com.taotao.boot.office.easyexcel.easyexcelconvert.core.ExcelListener;
import com.taotao.boot.office.easyexcel.easyexcelconvert.core.ExcelResult;
import com.taotao.cloud.sys.biz.model.excel.imports.UserImport;
import com.taotao.cloud.sys.biz.service.business.IUserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统用户自定义导入
 *
 * <pre class="code">
 *     {@code @Log(title}  = "用户管理", businessType = BusinessType.IMPORT)
 *     {@code @SaCheckPermission("system:user:import")}
 *     {@code @PostMapping(value} = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
 *     public R<Void> importData(MultipartFile file, boolean updateSupport) throws Exception {
 *         ExcelResult<SysUserImportVo> result = ExcelUtil.importExcel(file.getInputStream(), SysUserImportVo.class, new SysUserImportListener(updateSupport));
 *         return R.ok(result.getAnalysis());
 *     }
 * </pre>
 *
 * @author Lion Li
 */
@Slf4j
public class SysUserImportListener extends AnalysisEventListener<UserImport> implements
	ExcelListener<UserImport> {

    private final IUserService userService;

    private String password;

    private final Boolean isUpdateSupport;

    private String operName;

    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    public SysUserImportListener(Boolean isUpdateSupport) {
        // String initPassword =
        // ContextUtils.getBean(ISysConfigService.class).selectConfigByKey("sys.user.initPassword");
        this.userService = ContextUtils.getBean(IUserService.class);
        // this.password = BCrypt.hashpw(initPassword);
        this.isUpdateSupport = isUpdateSupport;
        // this.operName = LoginHelper.getUsername();
    }

    @Override
    public void invoke(UserImport userVo, AnalysisContext context) {
        // SysUser user = this.userService.selectUserByUserName(userVo.getUserName());
        // try {
        //	// 验证是否存在这个用户
        //	if (ObjectUtil.isNull(user)) {
        //		user = BeanUtil.toBean(userVo, SysUser.class);
        //		ValidatorUtils.validate(user);
        //		user.setPassword(password);
        //		user.setCreateBy(operName);
        //		userService.insertUser(user);
        //		successNum++;
        //		successMsg.append("<br/>").append(successNum).append("、账号 ")
        //			.append(user.getUserName()).append(" 导入成功");
        //	} else if (isUpdateSupport) {
        //		ValidatorUtils.validate(user);
        //		user.setUpdateBy(operName);
        //		userService.updateUser(user);
        //		successNum++;
        //		successMsg.append("<br/>").append(successNum).append("、账号 ")
        //			.append(user.getUserName()).append(" 更新成功");
        //	} else {
        //		failureNum++;
        //		failureMsg.append("<br/>").append(failureNum).append("、账号 ")
        //			.append(user.getUserName()).append(" 已存在");
        //	}
        // } catch (Exception e) {
        //	failureNum++;
        //	String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
        //	failureMsg.append(msg).append(e.getMessage());
        //	log.error(msg, e);
        // }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {}

    @Override
    public ExcelResult<UserImport> getExcelResult() {
        return new ExcelResult<UserImport>() {

            @Override
            public String getAnalysis() {
                if (failureNum > 0) {
                    failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                    throw new BusinessException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
                }
                return successMsg.toString();
            }

            @Override
            public List<UserImport> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }
}
