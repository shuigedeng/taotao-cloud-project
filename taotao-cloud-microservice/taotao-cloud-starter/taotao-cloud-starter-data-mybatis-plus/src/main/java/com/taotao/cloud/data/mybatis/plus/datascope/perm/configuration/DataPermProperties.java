package com.taotao.cloud.data.mybatis.plus.datascope.perm.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**   
* 数据权限配置
* @author xxm  
* @date 2021/12/3 
*/
@Getter
@Setter
@ConfigurationProperties("bootx.starter.data-perm")
public class DataPermProperties {

    /** 开启字段加密 */
    private boolean enableFieldDecrypt = true;

    /** 字段加密key 需要符合AES秘钥的要求 */
    private String fieldDecryptKey = "UCrtxSCwYZNCIlav";

    /** 开启数据权限 */
    private boolean enableDataPerm = true;

    /** 数据权限配置 */
    private DataPerm dataPerm = new DataPerm();

    /** 开启查询字段权限 */
    private boolean enableSelectFieldPerm = true;


    /**
     * 数据权限
     */
    @Getter
    @Setter
    public static class DataPerm {
        /** 表名 */
        private String table = "iam_user_dept";

        /** 查询字段(用户字段名) */
        private String queryField = "user_id";

        /** 条件字段(筛选条件字段名) */
        private String whereField = "dept_id";
    }

}
