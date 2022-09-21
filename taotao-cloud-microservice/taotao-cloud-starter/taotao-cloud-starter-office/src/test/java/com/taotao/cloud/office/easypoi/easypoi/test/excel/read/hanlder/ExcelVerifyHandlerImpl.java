/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.taotao.cloud.office.easypoi.easypoi.test.excel.read.hanlder;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.taotao.cloud.office.easypoi.test.entity.ExcelVerifyEntity;
import org.apache.commons.lang3.StringUtils;

public class ExcelVerifyHandlerImpl implements IExcelVerifyHandler<ExcelVerifyEntity> {

	@Override
	public ExcelVerifyHandlerResult verifyHandler(ExcelVerifyEntity obj) {
		StringBuilder builder = new StringBuilder();
		if (StringUtils.isNotEmpty(obj.getEmail())) {
			builder.append("Email must null;");
		}
		if (obj.getMax() > 15) {
			builder.append("max must lt 15;");
		}
		return new ExcelVerifyHandlerResult(false, builder.toString());
	}

}
