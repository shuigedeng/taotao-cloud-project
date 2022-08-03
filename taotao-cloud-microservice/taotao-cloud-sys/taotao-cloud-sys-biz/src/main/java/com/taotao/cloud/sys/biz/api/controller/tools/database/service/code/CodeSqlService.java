package com.taotao.cloud.sys.biz.api.controller.tools.database.service.code;

import com.taotao.cloud.sys.biz.api.controller.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.JdbcMetaService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.TableSearchService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos.CodeFromSqlParam;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos.JavaBeanInfo;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.dtos.ProjectGenerateConfig;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.rename.DefaultRenameStragtegy;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.rename.MetaClassAdapter;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.code.rename.RenameStrategy;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.aspect.JdbcConnection;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.sqlparser.ExtendFindTable;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.sqlparser.FindTable;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.sqlparser.TablesFinder;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.sqlparser.WhereTableColumnFinder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 一句 sql 生成相应代码
 */
@Service
@Slf4j
public class CodeSqlService {

    @Autowired
    private JdbcMetaService jdbcMetaService;
    @Autowired
    private TableSearchService tableSearchService;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private Configuration configuration;

    private CCJSqlParserManager parserManager = new CCJSqlParserManager();

    /**
     * 生成代码的目录
     */
    public static final String BASE_GENERATE_DIR = "code/generate/";

    @JdbcConnection
    public File generateCode(CodeFromSqlParam codeFromSqlParam) throws JSQLParserException, IOException, SQLException {
        Select select = (Select) parserManager.parse(new StringReader(codeFromSqlParam.getSql()));

        // 所有数据表查找
        TablesFinder tablesFinder = new TablesFinder();
        final List<FindTable> tableList = tablesFinder.getTableList(select);

        // 查询数据表的列元数据信息
        List<ExtendFindTable> extendFindTables = new ArrayList<>();
        final List<String> tableNames = tableList.stream().map(FindTable::getName).collect(Collectors.toList());
        final List<TableMeta> tables = tableSearchService.getTables(codeFromSqlParam.getConnName(), codeFromSqlParam.getNamespace(), tableNames);
        final List<TableMetaData> tableMetaData = jdbcMetaService.tablesExtend(codeFromSqlParam.getConnName(), tables);
        final Map<String, TableMetaData> tableNameMetaMap = tableMetaData.stream().collect(Collectors.toMap(tableMeta -> tableMeta.getTable().getActualTableName().getTableName(), Function.identity()));
        for (FindTable findTable : tableList) {
            final ExtendFindTable extendFindTable = new ExtendFindTable(findTable);
            extendFindTables.add(extendFindTable);

            final TableMetaData tableMeta = tableNameMetaMap.get(findTable.getName());
            if (tableMeta != null){
                extendFindTable.setTableMeta(tableMeta);
            }
        }

        // 数据表别名映射
        final Map<String, ExtendFindTable> tableAliasMap = extendFindTables.stream().collect(Collectors.toMap(extendFindTable -> extendFindTable.getFindTable().getAlias(), Function.identity()));

        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        final List<SelectItem> selectItems = plainSelect.getSelectItems();
        final Expression where = plainSelect.getWhere();

        final File generateBaseDir = fileManager.mkTmpDir(BASE_GENERATE_DIR + "/sqlToCode/" + System.currentTimeMillis());

        final ProjectGenerateConfig.PackageConfig packageConfig = codeFromSqlParam.getPackageConfig();
        final String bizName = codeFromSqlParam.getBizName();
        // 生成输出项
        generateOutputDto(selectItems, tableAliasMap, generateBaseDir, packageConfig, bizName);

        // 生成查询项
        generateQueryParamDto(where, tableAliasMap, generateBaseDir, packageConfig, bizName);

        // 生成 sql 文件
        final File file = new File(generateBaseDir, "sql.sql");
        FileUtils.writeStringToFile(file, codeFromSqlParam.getSql(), StandardCharsets.UTF_8);

        return generateBaseDir;
    }

    /**
     * 生成查询参数信息
     * @return
     */
    private File generateQueryParamDto(Expression where, Map<String, ExtendFindTable> tableAliasMap, File generateBaseDir, ProjectGenerateConfig.PackageConfig packageConfig, String bizName) throws IOException {
        WhereTableColumnFinder whereTableColumnFinder = new WhereTableColumnFinder(tableAliasMap);
        final MultiValueMap<String, Column> tableColumnsMap = whereTableColumnFinder.getTableColumnsMap(where);
        // 最后, 对收集到的数据构建 bean 对象, 然后使用模板生成代码
        return processBeanDto(tableColumnsMap,generateBaseDir,packageConfig.getParam(),bizName);
    }

    /**
     * 生成 vo 信息
     */
    private File generateOutputDto(List<SelectItem> selectItems, Map<String, ExtendFindTable> tableAliasMap, File generateBaseDir, ProjectGenerateConfig.PackageConfig packageConfig, String bizName) throws IOException {
        // 找到每个数据表有哪些列需要输出 tableName => List<Column>
        MultiValueMap<String, Column> tableColumnsMap = new LinkedMultiValueMap<>();

        for (SelectItem selectItem : selectItems) {
            if (selectItem instanceof AllColumns){
                for (ExtendFindTable extendFindTable : tableAliasMap.values()) {
                    final List<Column> columns = extendFindTable.getTableMeta().getColumns();
                    tableColumnsMap.put(extendFindTable.getFindTable().getName(),columns);
                }
                break;
            }else if (selectItem instanceof AllTableColumns){
                AllTableColumns allTableColumns = (AllTableColumns) selectItem;
                final String tableAlias = allTableColumns.getTable().getName();
                final ExtendFindTable extendFindTable = tableAliasMap.get(tableAlias);
                tableColumnsMap.put(extendFindTable.getFindTable().getName(),extendFindTable.getTableMeta().getColumns());
            }else if (selectItem instanceof SelectExpressionItem){
                SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
                final Expression expression = selectExpressionItem.getExpression();
                if (!(expression instanceof net.sf.jsqlparser.schema.Column)){
                    log.warn("仅支持查询列, 不支持查询列中有子查询的情况, 在 {}",selectItem.toString());
                    continue;
                }

                net.sf.jsqlparser.schema.Column column = (net.sf.jsqlparser.schema.Column) expression;
                if (tableAliasMap.size() == 1){
                    // 如果只有一张表, 则所有列都是这张表的, 不用考虑有没有别名的情况
                    final ExtendFindTable extendFindTable = tableAliasMap.values().iterator().next();
                    final Map<String, Column> columnMap = extendFindTable.getTableMeta().getColumns().stream().collect(Collectors.toMap(Column::getColumnName, Function.identity()));
                    tableColumnsMap.add(extendFindTable.getFindTable().getName(),columnMap.get(column.getColumnName()));
                    continue;
                }

                // 如果有多张表的情况, 有别名时可以快速找到是哪张表
                if (column.getTable() != null && StringUtils.isNotBlank(column.getTable().getName())){
                    final ExtendFindTable extendFindTable = tableAliasMap.get(column.getTable().getName());
                    final Map<String, Column> columnMap = extendFindTable.getTableMeta().getColumns().stream().collect(Collectors.toMap(Column::getColumnName, Function.identity()));
                    tableColumnsMap.add(extendFindTable.getFindTable().getName(),columnMap.get(column.getColumnName()));
                    continue;
                }

                // 没有别名时, 只能一张表一张表去查找, 如果多张表有这个列, 则以第一张表找到的为准
                A:for (ExtendFindTable extendFindTable : tableAliasMap.values()) {
                    final List<Column> columns = extendFindTable.getTableMeta().getColumns();
                    for (Column currColumn : columns) {
                        if (currColumn.getColumnName().equalsIgnoreCase(column.getColumnName())){
                            tableColumnsMap.add(extendFindTable.getFindTable().getName(),currColumn);
                            break A;
                        }
                    }
                }

            }
        }

        // 最后, 对收集到的数据构建 bean 对象, 然后使用模板生成代码
        return processBeanDto(tableColumnsMap,generateBaseDir,packageConfig.getVo(),bizName);
    }

    MetaClassAdapter metaClassAdapter = new MetaClassAdapter();

    private File processBeanDto(MultiValueMap<String, Column> tableColumnsMap, File generateBaseDir, String packageName, String bizName) throws IOException {
        RenameStrategy renameStrategy = new DefaultRenameStragtegy();
        final File output = new File(generateBaseDir, packageName);
        List<String> childClasses = new ArrayList<>();
        for (Map.Entry<String, List<Column>> stringListEntry : tableColumnsMap.entrySet()) {
            final String tableName = stringListEntry.getKey();
            final List<Column> columns = stringListEntry.getValue();

            JavaBeanInfo javaBeanInfo = new JavaBeanInfo(metaClassAdapter.mappingClassName(tableName));
            childClasses.add(javaBeanInfo.getClassName());
            for (Column column : columns) {
                String columnName = column.getColumnName();
                String fieldName = metaClassAdapter.mappingProperty(columnName);

                // 获取 java 类型
                int dataType = column.getDataType();
                int columnSize = column.getColumnSize();
                int decimalDigits = column.getDecimalDigits();
                String remark = column.getRemark();
                JDBCType jdbcType = JDBCType.valueOf(dataType);
                boolean isKey = false;
                if (multiValueMap.get(String.class).contains(jdbcType)){
                    if (jdbcType == JDBCType.CHAR && columnSize == 1){
                        javaBeanInfo.addField(new JavaBeanInfo.BeanField(column,Character.class.getSimpleName(),fieldName,remark,isKey));
                        continue;
                    }
                    javaBeanInfo.addField(new JavaBeanInfo.BeanField(column,"String",fieldName,remark,isKey));
                    continue;
                }
                if (multiValueMap.get(Date.class).contains(jdbcType)){
                    javaBeanInfo.addImport("java.util.Date");
                    javaBeanInfo.addField(new JavaBeanInfo.BeanField(column,"Date",fieldName,remark,isKey));
                    continue;
                }
                if (multiValueMap.get(Integer.class).contains(jdbcType)){
                    // smallint , tinyint , integer 都认为是 int
                    javaBeanInfo.addField(new JavaBeanInfo.BeanField(column,"Integer",fieldName,remark,isKey));
                    continue;
                }
                if (jdbcType == JDBCType.BIGINT){
                    javaBeanInfo.addField(new JavaBeanInfo.BeanField(column,"Long",fieldName,remark,isKey));
                    continue;
                }
                if (multiValueMap.get(Double.class).contains(jdbcType)){
                    javaBeanInfo.addImport(BigDecimal.class.getName());
                    javaBeanInfo.addField(new JavaBeanInfo.BeanField(column,"BigDecimal",fieldName,remark,isKey));
                    continue;
                }

            }
            generateJavaBean(packageName, output, javaBeanInfo);
        }

        if (tableColumnsMap.size() > 1){
            // 如果超过一个实体, 需要再组合一下
            JavaBeanInfo javaBeanInfo = new JavaBeanInfo(StringUtils.capitalize(bizName));
            for (String childClass : childClasses) {
                final String classWholeName = packageName + "." + childClass;
                javaBeanInfo.addImport(classWholeName);
                javaBeanInfo.addField(new JavaBeanInfo.BeanField(null,childClass,StringUtils.uncapitalize(childClass), "{@link "+classWholeName+"}",false));
            }
            generateJavaBean(packageName, output, javaBeanInfo);
        }

        return output;
    }

    private void generateJavaBean(String packageName, File output, JavaBeanInfo javaBeanInfo) throws IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("bean", javaBeanInfo);
        context.put("packageName", packageName);
        // 准备 context
        File pojoFile = new File(output, javaBeanInfo.getClassName() + ".java");
        Template pojoTemplate = configuration.getTemplate("code/entity.java.ftl");
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(pojoFile));){
            pojoTemplate.process(context, outputStreamWriter);
        } catch (TemplateException e) {
            log.error("javaBeanBuild template error : {}",e.getMessage(),e);
        }
    }

}
