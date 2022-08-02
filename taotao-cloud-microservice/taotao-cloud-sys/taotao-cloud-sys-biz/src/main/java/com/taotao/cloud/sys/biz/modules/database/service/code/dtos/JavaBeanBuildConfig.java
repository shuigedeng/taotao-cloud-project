package com.taotao.cloud.sys.biz.modules.database.service.code.dtos;

import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;
import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class JavaBeanBuildConfig {
    /**
     * 连接名
     */
    @NotNull
    private String connName;
    /**
     * 数据库命名空间
     */
    private Namespace namespace;
    /**
     * 需要构建的数据表列表
     */
    private List<String> tableNames = new ArrayList<>();

    /**
     * 是否添加 lombok
     */
    private boolean lombok;
    /**
     * 是否添加 swagger
     */
    private boolean swagger2;
    /**
     * 是否添加 jpa
     */
    private boolean persistence;
    /**
     * 是否实现 serializer
     */
    private boolean serializer;
    /**
     * 父类
     */
    private String supperClass;
    /**
     * 排除列
     */
    private List<String> exclude = new ArrayList<>();
    /**
     * 重命名策略名称
     */
    private String renameStrategy;
    /**
     * 包名
     */
    @NotNull
    private String packageName;

    @Tolerate
    public JavaBeanBuildConfig() {
    }
}
