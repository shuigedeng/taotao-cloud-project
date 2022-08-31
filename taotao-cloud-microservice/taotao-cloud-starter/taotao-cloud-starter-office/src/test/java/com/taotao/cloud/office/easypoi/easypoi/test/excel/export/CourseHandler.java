package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.test.entity.CourseEntity;
import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;

public class CourseHandler extends ExcelDataHandlerDefaultImpl<CourseEntity> {

    @Override
    public Object exportHandler(CourseEntity obj, String name, Object value) {
        if (name.equals("课程名称")) {
            return String.valueOf(value) + "课程";
        }
        return super.exportHandler(obj, name, value);
    }

    @Override
    public Object importHandler(CourseEntity obj, String name, Object value) {
        return super.importHandler(obj, name, value);
    }

}
