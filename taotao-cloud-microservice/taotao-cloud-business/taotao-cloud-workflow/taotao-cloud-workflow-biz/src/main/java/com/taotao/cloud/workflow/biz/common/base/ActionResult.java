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

package com.taotao.cloud.workflow.biz.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.taotao.cloud.workflow.biz.common.base.vo.DataInterfacePageListVO;
import com.taotao.cloud.workflow.biz.common.base.vo.PageListVO;
import com.taotao.cloud.workflow.biz.common.base.vo.PaginationVO;
import com.taotao.cloud.workflow.biz.common.constant.MsgCode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** 返回值 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionResult<T> {

    @Schema(description = "状态码")
    private Integer code;

    @Schema(description = "返回信息")
    private String msg;

    @Schema(description = "返回数据")
    private T data;

    public static <T> ActionResult<T> success() {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setCode(200);
        jsonData.setMsg(MsgCode.SU000.get());
        return jsonData;
    }

    public static <T> ActionResult<T> success(String msg) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setCode(200);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static <T> ActionResult<PageListVO<T>> page(List<T> list, PaginationVO pagination) {
        ActionResult<PageListVO<T>> jsonData = new ActionResult<>();
        PageListVO<T> vo = new PageListVO<>();
        vo.setList(list);
        vo.setPagination(pagination);
        jsonData.setData(vo);
        jsonData.setCode(200);
        jsonData.setMsg(MsgCode.SU000.get());
        return jsonData;
    }

    public static <T> ActionResult<DataInterfacePageListVO<T>> page(
            List<T> list, PaginationVO pagination, String dataProcessing) {
        ActionResult<DataInterfacePageListVO<T>> jsonData = new ActionResult<>();
        DataInterfacePageListVO<T> vo = new DataInterfacePageListVO<>();
        vo.setList(list);
        vo.setPagination(pagination);
        vo.setDataProcessing(dataProcessing);
        jsonData.setCode(200);
        jsonData.setData(vo);
        jsonData.setMsg(MsgCode.SU000.get());
        return jsonData;
    }

    public static ActionResult<Object> success(String msg, Object object) {
        ActionResult<Object> jsonData = new ActionResult<>();
        jsonData.setData(object);
        jsonData.setCode(200);
        jsonData.setMsg(msg);
        return jsonData;
    }

    public static <T> ActionResult<T> success(T object) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setData(object);
        jsonData.setCode(200);
        jsonData.setMsg(MsgCode.SU000.get());
        return jsonData;
    }

    public static <T> ActionResult<T> fail(Integer code, String message) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setCode(code);
        jsonData.setMsg(message);
        return jsonData;
    }

    public static ActionResult<String> fail(String msg, String data) {
        ActionResult<String> jsonData = new ActionResult<>();
        jsonData.setMsg(msg);
        jsonData.setData(data);
        return jsonData;
    }

    public static <T> ActionResult<T> fail(String msg) {
        ActionResult<T> jsonData = new ActionResult<>();
        jsonData.setMsg(msg);
        jsonData.setCode(400);
        return jsonData;
    }

    /*= different =*/

    public static ActionResult<Map<String, Object>> success(Object rows, PageModel pageModel) {
        ActionResult<Map<String, Object>> jsonData = new ActionResult<>();
        Map<String, Object> map = new HashMap<>(16);
        map.put("page", pageModel.getPage());
        map.put("records", pageModel.getRecords());
        map.put("rows", rows);
        map.put("total", pageModel.getTotal());
        jsonData.setData(map);
        jsonData.setCode(200);
        jsonData.setMsg(MsgCode.SU000.get());
        return jsonData;
    }
}
