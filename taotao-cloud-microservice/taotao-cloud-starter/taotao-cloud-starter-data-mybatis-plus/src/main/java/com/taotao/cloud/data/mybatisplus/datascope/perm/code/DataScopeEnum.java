package com.taotao.cloud.data.mybatisplus.datascope.perm.code;

import com.taotao.cloud.common.exception.BusinessException;

import java.util.Arrays;

/**
* 数据范围权限类型
*/
public enum DataScopeEnum {
    /** 自己的数据 */
    SELF(1),
    /** 指定用户级别 */
    USER_SCOPE(2),
    /** 指定部门级别 */
    DEPT_SCOPE(3),
    /** 指定部门与用户级别 */
    DEPT_AND_USER_SCOPE(4),
    /** 全部数据 */
    ALL_SCOPE(5),
    /** 所在部门 */
    BELONG_DEPT(6),
    /** 所在及下级部门 */
    BELONG_DEPT_AND_SUB(7);
    private final int code;

	DataScopeEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	/**
     * 根据数字编号获取
     */
    public static DataScopeEnum findByCode(int code){
        return Arrays.stream(DataScopeEnum.values())
                .filter(e -> e.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new BusinessException("不支持的数据权限类型"));
    }

}
