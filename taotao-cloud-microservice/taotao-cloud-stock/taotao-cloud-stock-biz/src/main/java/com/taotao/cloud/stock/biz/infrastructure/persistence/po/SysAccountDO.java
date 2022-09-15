package com.taotao.cloud.stock.biz.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户Token DO
 *
 * @author shuigedeng
 * @date 2021-02-09
 */
@Data
@TableName("sys_account")
public class SysAccountDO extends BaseDO {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

}
