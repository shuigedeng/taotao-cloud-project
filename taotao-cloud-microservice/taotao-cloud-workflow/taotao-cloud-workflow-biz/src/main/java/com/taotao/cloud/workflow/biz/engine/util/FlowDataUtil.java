package com.taotao.cloud.workflow.biz.engine.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.workflow.biz.model.FormAllModel;
import com.taotao.cloud.workflow.biz.model.FormColumnModel;
import com.taotao.cloud.workflow.biz.model.FormColumnTableModel;
import com.taotao.cloud.workflow.biz.model.FormEnum;
import com.taotao.cloud.workflow.biz.model.FormMastTableModel;
import com.taotao.cloud.workflow.biz.model.RecursionForm;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlowDataUtil {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private DataSourceUtil dataSourceUtil;
    @Autowired
    private ConfigValueUtil configValueUtil;
    @Autowired
    private ServiceAllUtil serviceUtil;


    /**
     * 获取有表的数据库连接
     *
     * @return
     */
    private Connection getTableConn(DbLinkEntity link) throws SQLException {
        Connection conn = null;
        if (link != null) {
            try {
                conn = ConnUtil.getConn(link);
            } catch (DataException e) {
                e.printStackTrace();
            }
        } else {
            String tenId = "";
            if (!Boolean.parseBoolean(configValueUtil.getMultiTenancy())) {
                tenId = dataSourceUtil.getDbName();
            } else {
                tenId = userProvider.get().getTenantDbConnectionString();
            }
            try {
                conn = ConnUtil.getConn(dataSourceUtil, tenId);
            } catch (DataException e) {
                e.printStackTrace();
            }
        }
        if (conn == null) {
            throw new SQLException("连接数据库失败");
        }
        return conn;
    }

    /**
     * 获取有子表数据
     *
     * @param sql sql语句
     * @return
     * @throws DataException
     */
    private List<Map<String, Object>> getTableList(Connection conn, String sql) throws WorkFlowException {
        try {
            List<Map<String, Object>> list = JdbcUtil.queryListLowercase(new PreparedStatementDTO(conn, sql));
            return list;
        } catch (DataException e) {
            throw new WorkFlowException(e.getMessage());
        }
    }

    /**
     * 获取主表数据
     *
     * @param sql sql语句
     * @return
     * @throws DataException
     */
    private Map<String, Object> getMast(Connection conn, String sql) throws WorkFlowException {
        try {
            Map<String, Object> mast = JdbcUtil.queryOne(new PreparedStatementDTO(conn, sql));
            Map<String, Object> mastData = new HashMap<>(16);
            for (String key : mast.keySet()) {
                mastData.put(key.toLowerCase(), mast.get(key));
            }
            return mastData;
        } catch (DataException e) {
            throw new WorkFlowException(e.getMessage());
        }
    }

    /**
     * 返回主键名称
     *
     * @param conn
     * @param mainTable
     * @return
     */
    private String getKey(Connection conn, String mainTable) throws SQLException {
        String pKeyName = "f_id";
        //catalog 数据库名
        String catalog = conn.getCatalog();
        @Cleanup ResultSet primaryKeyResultSet = conn.getMetaData().getPrimaryKeys(catalog, null, mainTable);
        while (primaryKeyResultSet.next()) {
            pKeyName = primaryKeyResultSet.getString("COLUMN_NAME");
        }
        primaryKeyResultSet.close();
        return pKeyName;
    }
    //---------------------------------------------信息---------------------------------------------

    public Map<String, Object> info(List<FieLdsModel> fieLdslist, FlowTaskEntity entity, List<TableModel> tableList, boolean convert, DbLinkEntity link) throws WorkFlowException {
        Map<String, Object> data = StringUtil.isNotEmpty(entity.getFlowFormContentJson()) ? JsonUtils.stringToMap(entity.getFlowFormContentJson()) : new HashMap<>(16);
        DataModel dataModel = new DataModel(data, fieLdslist, tableList, entity.getId(), link, convert);
        return this.info(dataModel);
    }

    /**
     * 信息
     *
     * @param dataModel
     * @return
     * @throws WorkFlowException
     */
    public Map<String, Object> info(DataModel dataModel) throws WorkFlowException {
        Map<String, Object> result = new HashMap<>();
        try {
            List<FieLdsModel> fieLdsModelList = dataModel.getFieLdsModelList();
            List<TableModel> tableModelList = dataModel.getTableModelList();
            RecursionForm recursionForm = new RecursionForm(fieLdsModelList, tableModelList);
            List<FormAllModel> formAllModel = new ArrayList<>();
            //递归遍历模板
            FormCloumnUtil.recursionForm(recursionForm, formAllModel);
            result = this.infoDataList(dataModel, formAllModel);
        } catch (WorkFlowException e) {
            log.error("查询异常：" + e.getMessage());
            throw new WorkFlowException(e.getMessage());
        }
        return result;
    }

    /**
     * 获取所有数据
     *
     * @param dataModel
     * @param formAllModel
     * @return
     * @throws WorkFlowException
     */
    private Map<String, Object> infoDataList(DataModel dataModel, List<FormAllModel> formAllModel) throws WorkFlowException {
        //处理好的数据
        Map<String, Object> result = new HashMap<>(16);
        List<TableModel> tableModelList = dataModel.getTableModelList();
        if (tableModelList.size() > 0) {
            result = this.tableData(dataModel, formAllModel);
        } else {
            result = this.data(dataModel, formAllModel);
        }
        return result;
    }

    /**
     * 有表数据
     *
     * @return
     * @throws WorkFlowException
     */
    private Map<String, Object> tableData(DataModel dataModel, List<FormAllModel> formAllModel) throws WorkFlowException {
        Map<String, Object> data = new HashMap<>();
        try {
            String mainId = dataModel.getMainId();
            DbLinkEntity link = dataModel.getLink();
            @Cleanup Connection conn = this.getTableConn(link);
            List<TableModel> tableList = dataModel.getTableModelList();
            Optional<TableModel> first = tableList.stream().filter(t -> "1".equals(t.getTypeId())).findFirst();
            if (!first.isPresent()) {
                throw new WorkFlowException(MsgCode.COD001.get());
            }
            String mastTableName = first.get().getTable();
            List<FormAllModel> mastForm = formAllModel.stream().filter(t -> FormEnum.mast.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
            List<String> mastFile = mastForm.stream().filter(t -> StringUtil.isNotEmpty(t.getFormColumnModel().getFieLdsModel().getVModel())).map(t -> t.getFormColumnModel().getFieLdsModel().getVModel()).collect(Collectors.toList());
            String pKeyName = this.getKey(conn, mastTableName);
            //主表数据
            String mastInfo = " select " + String.join(",", mastFile) + " from " + mastTableName + " where " + pKeyName + " = '" + mainId + "'";
            Map<String, Object> mastData = getMast(conn, mastInfo);
            Map<String, Object> mastDataAll = new HashMap<>();
            for (String key : mastData.keySet()) {
                Object value = mastData.get(key);
                FormAllModel formAll = mastForm.stream().filter(t -> key.equals(t.getFormColumnModel().getFieLdsModel().getVModel().toLowerCase())).findFirst().orElse(null);
                if (formAll != null) {
                    FieLdsModel fieLdsModel = formAll.getFormColumnModel().getFieLdsModel();
                    String dataKey = fieLdsModel.getVModel();
                    value = this.info(fieLdsModel, value, true);
                    mastDataAll.put(dataKey, value);
                }
            }
            data.putAll(mastDataAll);
            //子表数据
            List<FormAllModel> tableForm = formAllModel.stream().filter(t -> FormEnum.table.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
            Map<String, Object> childData = new HashMap<>();
            for (FormAllModel model : tableForm) {
                FormColumnTableModel childList = model.getChildList();
                String tableName = childList.getTableName();
                String tableModel = childList.getTableModel();
                String childKey = this.getKey(conn, tableName);
                List<String> childFile = childList.getChildList().stream().filter(t -> StringUtil.isNotEmpty(t.getFieLdsModel().getVModel())).map(t -> t.getFieLdsModel().getVModel()).collect(Collectors.toList());
                Optional<TableModel> first1 = tableList.stream().filter(t -> t.getTable().equals(tableName)).findFirst();
                if (!first1.isPresent()) {
                    throw new WorkFlowException(MsgCode.COD001.get());
                }
                TableModel table = first1.get();
                String tableInfo = "select " + String.join(",", childFile) + " from " + tableName + " where " + table.getTableField() + "='" + mainId + "' order by " + childKey + " asc";
                List<Map<String, Object>> tableDataList = getTableList(conn, tableInfo);
                List<Map<String, Object>> tableDataAll = new LinkedList<>();
                //子表赋值
                for (Map<String, Object> tableData : tableDataList) {
                    Map<String, Object> childDataOne = new HashMap<>();
                    for (String key : tableData.keySet()) {
                        Object value = tableData.get(key);
                        FieLdsModel fieLdsModel = childList.getChildList().stream().filter(t -> key.equals(t.getFieLdsModel().getVModel().toLowerCase())).map(t -> t.getFieLdsModel()).findFirst().orElse(null);
                        value = this.info(fieLdsModel, value, true);
                        String dataKey = fieLdsModel.getVModel();
                        childDataOne.put(dataKey, value);
                    }
                    tableDataAll.add(childDataOne);
                }
                childData.put(tableModel, tableDataAll);
            }
            data.putAll(childData);
            //副表
            Map<String, List<FormAllModel>> mastTableAll = formAllModel.stream().filter(t -> FormEnum.mastTable.getMessage().equals(t.getJnpfKey())).collect(Collectors.groupingBy(e -> e.getFormMastTableModel().getTable()));
            for (String key : mastTableAll.keySet()) {
                Optional<TableModel> first1 = tableList.stream().filter(t -> t.getTable().equals(key)).findFirst();
                if (!first1.isPresent()) {
                    throw new WorkFlowException(MsgCode.COD001.get());
                }
                TableModel tableModel = first1.get();
                String table = tableModel.getTable();
                List<FormAllModel> mastTableList = mastTableAll.get(key);
                List<String> field = mastTableList.stream().filter(t -> StringUtil.isNotEmpty(t.getFormMastTableModel().getField())).map(t -> t.getFormMastTableModel().getField()).collect(Collectors.toList());
                String mastTableInfo = "select " + String.join(",", field) + " from " + table + " where " + tableModel.getTableField() + "='" + mainId + "'";
                Map<String, Object> dataAll = getMast(conn, mastTableInfo);
                Map<String, Object> mastTable = new HashMap<>();
                for (String mastKey : dataAll.keySet()) {
                    Object value = dataAll.get(mastKey);
                    FieLdsModel fieLdsModel = mastTableList.stream().filter(t -> mastKey.equals(t.getFormMastTableModel().getField().toLowerCase())).map(t -> t.getFormMastTableModel().getMastTable().getFieLdsModel()).findFirst().orElse(null);
                    value = this.info(fieLdsModel, value, true);
                    String dataKey = fieLdsModel.getVModel();
                    mastTable.put(dataKey, value);
                }
                data.putAll(mastTable);
            }
        } catch (SQLException e) {
            log.error("查询异常：{}", e.getMessage());
            throw new WorkFlowException(e.getMessage());
        }
        return data;
    }

    /**
     * 无表数据
     *
     * @return
     */
    private Map<String, Object> data(DataModel dataModel, List<FormAllModel> formAllModel) {
        Map<String, Object> dataMap = dataModel.getDataNewMap();
        Map<String, Object> result = new HashMap<>();
        List<FormAllModel> mastForm = formAllModel.stream().filter(t -> FormEnum.mast.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
        List<FormAllModel> tableForm = formAllModel.stream().filter(t -> FormEnum.table.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
        for (String key : dataMap.keySet()) {
            FormAllModel model = mastForm.stream().filter(t -> key.equals(t.getFormColumnModel().getFieLdsModel().getVModel())).findFirst().orElse(null);
            if (model != null) {
                FieLdsModel fieLdsModel = model.getFormColumnModel().getFieLdsModel();
                Object data = dataMap.get(key);
                data = this.info(fieLdsModel, data, false);
                result.put(key, data);
            } else {
                FormAllModel childModel = tableForm.stream().filter(t -> key.equals(t.getChildList().getTableModel())).findFirst().orElse(null);
                if (childModel != null) {
                    String childKeyName = childModel.getChildList().getTableModel();
                    List<Map<String, Object>> childDataMap = (List<Map<String, Object>>) dataMap.get(key);
                    List<Map<String, Object>> childdataAll = new ArrayList<>();
                    for (Map<String, Object> child : childDataMap) {
                        Map<String, Object> tablValue = new HashMap<>(16);
                        for (String childKey : child.keySet()) {
                            FormColumnModel columnModel = childModel.getChildList().getChildList().stream().filter(t -> childKey.equals(t.getFieLdsModel().getVModel())).findFirst().orElse(null);
                            if (columnModel != null) {
                                FieLdsModel fieLdsModel = columnModel.getFieLdsModel();
                                Object childValue = child.get(childKey);
                                childValue = this.info(fieLdsModel, childValue, false);
                                tablValue.put(childKey, childValue);
                            }
                        }
                        childdataAll.add(tablValue);
                    }
                    result.put(childKeyName, childdataAll);
                }
            }
        }
        return result;
    }

    /**
     * 系统转换赋值
     **/
    private Object info(FieLdsModel fieLdsModel, Object dataValue, boolean isTable) {
        Object value = dataValue;
        String jnpfKey = fieLdsModel.getConfig().getJnpfKey();
        String format = fieLdsModel.getFormat();
        boolean multiple = fieLdsModel.getMultiple();
        String showLevel = fieLdsModel.getShowLevel();
        switch (jnpfKey) {
            case JnpfKeyConsts.CURRORGANIZE:
            case JnpfKeyConsts.CURRDEPT:
                if (ObjectUtil.isNotEmpty(value)) {
                    OrganizeEntity organizeEntity = serviceUtil.getOrganizeInfo(String.valueOf(value));
                    if ("all".equals(showLevel)) {
                        if (organizeEntity != null) {
                            List<OrganizeEntity> organizeList = serviceUtil.getOrganizeId(organizeEntity.getId());
                            value = organizeList.stream().map(OrganizeEntity::getFullName).collect(Collectors.joining("/"));
                        }
                    } else {
                        if (organizeEntity != null) {
                            value = organizeEntity.getFullName();
                        }
                    }
                }
                break;
            case JnpfKeyConsts.CREATEUSER:
            case JnpfKeyConsts.MODIFYUSER:
                if (ObjectUtil.isNotEmpty(value)) {
                    UserEntity userEntity = serviceUtil.getUserInfo(String.valueOf(value));
                    if (userEntity != null) {
                        value = userEntity.getRealName();
                    }
                }
                break;
            case JnpfKeyConsts.CURRPOSITION:
                if (ObjectUtil.isNotEmpty(value)) {
                    PositionEntity positionEntity = serviceUtil.getPositionInfo(String.valueOf(value));
                    if (positionEntity != null) {
                        value = positionEntity.getFullName();
                    }
                }
                break;
            case JnpfKeyConsts.UPLOADFZ:
            case JnpfKeyConsts.UPLOADIMG:
                if (value == null) {
                    value = new ArrayList<>();
                } else {
                    if (isTable) {
                        value = JsonUtils.getJsonToListMap(String.valueOf(value));
                    }
                }
                break;
            case JnpfKeyConsts.CHECKBOX:
            case JnpfKeyConsts.DATERANGE:
            case JnpfKeyConsts.TIMERANGE:
                if (value == null) {
                    value = new ArrayList<>();
                } else {
                    if (isTable) {
                        value = JsonUtils.getJsonToList(String.valueOf(value), String.class);
                    }
                }
                break;
            case JnpfKeyConsts.COMSELECT:
            case JnpfKeyConsts.ADDRESS:
                if (isTable) {
                    if (multiple) {
                        value = JsonUtils.getJsonToBean(String.valueOf(value), String[][].class);
                    } else {
                        value = JsonUtils.getJsonToList(String.valueOf(value), String.class);
                    }
                }
                break;
            case JnpfKeyConsts.SELECT:
            case JnpfKeyConsts.USERSELECT:
            case JnpfKeyConsts.DEPSELECT:
            case JnpfKeyConsts.POSSELECT:
                if (isTable) {
                    if (multiple) {
                        value = JsonUtils.getJsonToList(String.valueOf(value), String.class);
                    }
                }
                break;
            case JnpfKeyConsts.DATE:
                if (isTable) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        value = sdf.parse(String.valueOf(value)).getTime();
                    } catch (Exception e) {
                        value = dataValue;
                    }
                }
                break;
            case JnpfKeyConsts.SLIDER:
            case JnpfKeyConsts.SWITCH:
                if (isTable) {
                    try {
                        value = Integer.valueOf(String.valueOf(value));
                    } catch (Exception e) {
                        value = dataValue;
                    }
                }
                break;
            case JnpfKeyConsts.CASCADER:
                if (value == null) {
                    value = new ArrayList<>();
                } else {
                    if (isTable) {
                        PropsBeanModel propsModel = JsonUtils.getJsonToBean(fieLdsModel.getProps().getProps(), PropsBeanModel.class);
                        if (propsModel.getMultiple()) {
                            value = JsonUtils.getJsonToBean(String.valueOf(value), String[][].class);
                        } else {
                            value = JsonUtils.getJsonToList(String.valueOf(value), String.class);
                        }
                    }
                }
                break;
            default:
                break;
        }
        return value;
    }

    //---------------------------------------------新增---------------------------------------------

    public Map<String, Object> create(Map<String, Object> allDataMap, List<FieLdsModel> fieLdsModelList, List<TableModel> tableModelList, String mainId, Map<String, String> billData, DbLinkEntity link) throws WorkFlowException {
        DataModel dataModel = new DataModel(allDataMap, fieLdsModelList, tableModelList, mainId, link, false);
        Map<String, Object> result = this.create(dataModel);
        return result;
    }

    /**
     * 新增数据处理
     **/
    private Map<String, Object> create(DataModel dataModel) throws WorkFlowException {
        try {
            List<FieLdsModel> fieLdsModelList = dataModel.getFieLdsModelList();
            List<TableModel> tableModelList = dataModel.getTableModelList();
            RecursionForm recursionForm = new RecursionForm(fieLdsModelList, tableModelList);
            List<FormAllModel> formAllModel = new ArrayList<>();
            //递归遍历模板
            FormCloumnUtil.recursionForm(recursionForm, formAllModel);
            //处理好的数据
            Map<String, Object> result = this.createDataList(dataModel, formAllModel);
            return result;
        } catch (Exception e) {
            log.error("新增异常：{}", e.getMessage());
            throw new WorkFlowException(e.getMessage());
        }
    }

    /**
     * 新增数据
     **/
    private Map<String, Object> createDataList(DataModel dataModel, List<FormAllModel> formAllModel) throws SQLException {
        //处理好的数据
        Map<String, Object> result = new HashMap<>(16);
        String mainId = dataModel.getMainId();
        Map<String, Object> dataNewMap = dataModel.getDataNewMap();
        List<TableModel> tableModelList = dataModel.getTableModelList();
        //有表数据处理
        if (tableModelList.size() > 0) {
            DbLinkEntity link = dataModel.getLink();
            boolean isOracle = (DbTypeUtil.checkOracle(dataSourceUtil) || DbTypeUtil.checkPostgre(dataSourceUtil));
            if (link != null) {
                isOracle = (DbTypeUtil.checkOracle(link) || DbTypeUtil.checkPostgre(link));
            }
            //系统数据
            @Cleanup Connection conn = this.getTableConn(link);
            conn.setAutoCommit(false);
            //子表
            this.createTable(formAllModel, tableModelList, isOracle, dataNewMap, conn, mainId, result);
            //副表
            this.createMastTable(formAllModel, tableModelList, isOracle, dataNewMap, conn, mainId, result);
            //主表
            this.createMast(formAllModel, tableModelList, isOracle, dataNewMap, conn, mainId, result);
        } else {
            //无表数据处理
            result = this.createAll(dataNewMap, formAllModel);
        }
        return result;
    }

    /**
     * 子表数据
     **/
    private void createTable(List<FormAllModel> formAllModel, List<TableModel> tableModelList, boolean isOracle, Map<String, Object> allDataMap, Connection conn, String mainId, Map<String, Object> result) throws SQLException {
        //子表
        List<FormAllModel> tableForm = formAllModel.stream().filter(t -> FormEnum.table.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
        Map<String, List<FormColumnModel>> childMap = new HashMap<>();
        Map<String, TableModel> chidTable = new HashMap<>();
        tableForm.stream().forEach(t -> {
            FormColumnTableModel childListAll = t.getChildList();
            String tableModel = childListAll.getTableModel();
            List<FormColumnModel> childList = childListAll.getChildList().stream().filter(g -> StringUtil.isNotEmpty(g.getFieLdsModel().getVModel())).collect(Collectors.toList());
            childMap.put(tableModel, childList);
            String tableName = childListAll.getTableName();
            Optional<TableModel> first = tableModelList.stream().filter(k -> k.getTable().equals(tableName)).findFirst();
            if (first.isPresent()) {
                TableModel childTable = first.get();
                chidTable.put(tableModel, childTable);
            }
        });
        for (String key : childMap.keySet()) {
            //子表数据
            List<Map<String, Object>> chidList = allDataMap.get(key) != null ? (List<Map<String, Object>>) allDataMap.get(key) : new ArrayList<>();
            List<FormColumnModel> formColumnModels = childMap.get(key);
            Map<String, FieLdsModel> columMap = new HashMap<>();
            //获取子表对象的类型
            for (FormColumnModel column : formColumnModels) {
                FieLdsModel fieLdsModel = column.getFieLdsModel();
                String vmodel = fieLdsModel.getVModel();
                columMap.put(vmodel, fieLdsModel);
            }
            //子表主键
            TableModel tableModel = chidTable.get(key);
            String table = tableModel.getTable();
            String childKeyName = this.getKey(conn, table);
            //关联字段
            Optional<TableModel> first = tableModelList.stream().filter(t -> t.getTable().equals(table)).findFirst();
            String mastKeyName = "";
            if (first.isPresent()) {
                mastKeyName = first.get().getTableField();
            }
            //sql组装
            List<List<Object>> childDataAll = new ArrayList<>();
            List<Map<String, Object>> childResultAll = new ArrayList<>();
            StringJoiner filedModel = new StringJoiner(",");
            StringJoiner filedValue = new StringJoiner(",");
            //添加字段只要保存一次
            boolean isFile = true;
            for (Map<String, Object> objectMap : chidList) {
                List<Object> childData = new ArrayList<>();
                Map<String, Object> childOneResult = new HashMap<>(16);
                for (String childKey : columMap.keySet()) {
                    FieLdsModel fieLdsModel = columMap.get(childKey);
                    String jnpfkey = fieLdsModel.getConfig().getJnpfKey();
                    Object data = objectMap.get(childKey);
                    //处理系统自动生成
                    data = this.create(fieLdsModel, data, true);
                    String value = (isOracle && (JnpfKeyConsts.DATE.equals(jnpfkey) || JnpfKeyConsts.MODIFYTIME.equals(jnpfkey) || JnpfKeyConsts.CREATETIME.equals(jnpfkey))) ? "to_date(?,'yyyy-mm-dd HH24:mi:ss')" : "?";
                    //添加数据
                    childData.add(data);
                    childOneResult.put(childKey, data);
                    //添加字段
                    if (isFile) {
                        filedModel.add(childKey);
                        filedValue.add(value);
                    }
                }
                //添加主键值和外键值
                childData.add(RandomUtil.uuId());
                childData.add(mainId);
                //添加主键和外键
                if (isFile) {
                    //sql主键
                    filedModel.add(childKeyName);
                    filedValue.add("?");
                    //关联字段
                    filedModel.add(mastKeyName);
                    filedValue.add("?");
                }
                isFile = false;
                //保存list值
                childResultAll.add(childOneResult);
                childDataAll.add(childData);
            }
            //返回值
            result.put(key, childResultAll);
            String[] del = new String[]{};
            String childSql = "insert into " + table + "(" + filedModel + ") values (" + filedValue + ")";
            this.sql(childSql, childDataAll, del, conn, false);
        }
    }

    /**
     * 副表数据
     **/
    private void createMastTable(List<FormAllModel> formAllModel, List<TableModel> tableModelList, boolean isOracle, Map<String, Object> allDataMap, Connection conn, String mainId, Map<String, Object> result) throws SQLException {
        //副表
        Map<String, List<FormAllModel>> mastTableAll = formAllModel.stream().filter(t -> FormEnum.mastTable.getMessage().equals(t.getJnpfKey())).collect(Collectors.groupingBy(e -> e.getFormMastTableModel().getTable()));
        for (String key : mastTableAll.keySet()) {
            Optional<TableModel> first = tableModelList.stream().filter(t -> t.getTable().equals(key)).findFirst();
            if (!first.isPresent()) {
                throw new SQLException(MsgCode.COD001.get());
            }
            TableModel tableModel = first.get();
            String tableModelTable = tableModel.getTable();
            String childKeyName = this.getKey(conn, tableModelTable);
            //关联字段
            String mastKeyName = tableModel.getTableField();
            List<FormAllModel> masTableList = mastTableAll.get(key);
            //新增字段
            StringJoiner filedModel = new StringJoiner(",");
            StringJoiner filedValue = new StringJoiner(",");
            List<Object> mastData = new LinkedList<>();
            for (FormAllModel model : masTableList) {
                FormMastTableModel formMastTableModel = model.getFormMastTableModel();
                FormColumnModel mastTable = formMastTableModel.getMastTable();
                FieLdsModel fieLdsModel = mastTable.getFieLdsModel();
                String mostTableKey = fieLdsModel.getVModel();
                if (StringUtil.isEmpty(mostTableKey)) {
                    continue;
                }
                String jnpfkey = fieLdsModel.getConfig().getJnpfKey();
                Object data = allDataMap.get(mostTableKey);
                //处理系统自动生成
                data = this.create(fieLdsModel, data, true);
                //返回值
                result.put(mostTableKey, data);
                //添加字段
                mastData.add(data);
                String field = formMastTableModel.getField();
                String value = (isOracle && (JnpfKeyConsts.DATE.equals(jnpfkey) || JnpfKeyConsts.MODIFYTIME.equals(jnpfkey) || JnpfKeyConsts.CREATETIME.equals(jnpfkey))) ? "to_date(?,'yyyy-mm-dd HH24:mi:ss')" : "?";
                filedModel.add(field);
                filedValue.add(value);
            }
            //sql主键
            mastData.add(RandomUtil.uuId());
            filedModel.add(childKeyName);
            filedValue.add("?");
            //关联字段
            mastData.add(mainId);
            filedModel.add(mastKeyName);
            filedValue.add("?");
            //新增sql语句
            String sql = "insert into " + tableModelTable + "(" + filedModel + ") values (" + filedValue + ")";
            List<List<Object>> data = new LinkedList<>();
            data.add(mastData);
            this.sql(sql, data, new String[]{}, conn, false);
        }
    }

    /**
     * 主表数据
     **/
    private void createMast(List<FormAllModel> formAllModel, List<TableModel> tableModelList, boolean isOracle, Map<String, Object> allDataMap, Connection conn, String mainId, Map<String, Object> result) throws SQLException {
        Optional<TableModel> first = tableModelList.stream().filter(t -> "1".equals(t.getTypeId())).findFirst();
        if (!first.isPresent()) {
            throw new SQLException(MsgCode.COD001.get());
        }
        TableModel tableModel = first.get();
        String mastTableName = tableModel.getTable();
        List<FormAllModel> mastForm = formAllModel.stream().filter(t -> FormEnum.mast.getMessage().equals(t.getJnpfKey())).filter(t -> StringUtil.isNotEmpty(t.getFormColumnModel().getFieLdsModel().getVModel())).collect(Collectors.toList());
        //新增字段
        StringJoiner filedModel = new StringJoiner(",");
        StringJoiner filedValue = new StringJoiner(",");
        List<Object> mastData = new LinkedList<>();
        String keyName = this.getKey(conn, mastTableName);
        for (FormAllModel model : mastForm) {
            FieLdsModel fieLdsModel = model.getFormColumnModel().getFieLdsModel();
            String jnpfkey = fieLdsModel.getConfig().getJnpfKey();
            String field = fieLdsModel.getVModel();
            Object data = allDataMap.get(field);
            //处理系统自动生成
            data = this.create(fieLdsModel, data, true);
            mastData.add(data);
            //添加字段
            String value = (isOracle && (JnpfKeyConsts.DATE.equals(jnpfkey) || JnpfKeyConsts.MODIFYTIME.equals(jnpfkey) || JnpfKeyConsts.CREATETIME.equals(jnpfkey))) ? "to_date(?,'yyyy-mm-dd HH24:mi:ss')" : "?";
            filedModel.add(field);
            filedValue.add(value);
            result.put(field, data);
        }
        //sql主键
        mastData.add(mainId);
        filedModel.add(keyName);
        filedValue.add("?");
        //新增sql语句
        String sql = "insert into " + mastTableName + "(" + filedModel + ") values (" + filedValue + ")";
        List<List<Object>> data = new LinkedList<>();
        data.add(mastData);
        this.sql(sql, data, new String[]{}, conn, true);
    }

    /**
     * 新增系统赋值
     **/
    private Object create(FieLdsModel fieLdsModel, Object dataValue, boolean isTable) {
        String jnpfKey = fieLdsModel.getConfig().getJnpfKey();
        String rule = fieLdsModel.getConfig().getRule();
        UserEntity userEntity = serviceUtil.getUserInfo(userProvider.get().getUserId());
        Object value = dataValue;
        switch (jnpfKey) {
            case JnpfKeyConsts.CREATEUSER:
                value = userEntity.getId();
                break;
            case JnpfKeyConsts.CREATETIME:
                value = DateUtil.getNow("+8");
                break;
            case JnpfKeyConsts.CURRORGANIZE:
            case JnpfKeyConsts.CURRDEPT:
                value = userEntity.getOrganizeId();
                break;
            case JnpfKeyConsts.MODIFYTIME:
                value = null;
                break;
            case JnpfKeyConsts.MODIFYUSER:
                value = null;
                break;
            case JnpfKeyConsts.CURRPOSITION:
                value = userEntity.getPositionId();
                break;
            case JnpfKeyConsts.BILLRULE:
                try {
                    value = serviceUtil.getBillNumber(rule);
                } catch (Exception e) {
                    value = null;
                }
                break;
            case JnpfKeyConsts.DATE:
                if (isTable) {
                    try {
                        value = DateUtil.dateToString(new Date(Long.valueOf(String.valueOf(dataValue))), "yyyy-MM-dd HH:mm:ss");
                    } catch (Exception e) {

                    }
                }
                break;
            case JnpfKeyConsts.NUM_INPUT:
                if (isTable) {
                    try {
                        value = new BigDecimal(String.valueOf(dataValue));
                    } catch (Exception e) {

                    }
                }
                break;
            default:
                if (isTable) {
                    if (value instanceof List) {
                        value = String.valueOf(value);
                    } else if (value instanceof CharSequence) {
                        if (StrUtil.isEmpty((CharSequence) value)) {
                            value = null;
                        }
                    }
                }
                break;
        }
        return value;
    }

    /**
     * 无表插入数据
     **/
    private Map<String, Object> createAll(Map<String, Object> dataNewMap, List<FormAllModel> formAllModel) {
        //处理好的数据
        Map<String, Object> result = new HashMap<>(16);
        List<FormAllModel> mastForm = formAllModel.stream().filter(t -> FormEnum.mast.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
        List<FormAllModel> tableForm = formAllModel.stream().filter(t -> FormEnum.table.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
        for (String key : dataNewMap.keySet()) {
            FormAllModel model = mastForm.stream().filter(t -> key.equals(t.getFormColumnModel().getFieLdsModel().getVModel())).findFirst().orElse(null);
            if (model != null) {
                FieLdsModel fieLdsModel = model.getFormColumnModel().getFieLdsModel();
                Object data = dataNewMap.get(key);
                //处理系统自动生成
                data = this.create(fieLdsModel, data, false);
                result.put(key, data);
            } else {
                FormAllModel childModel = tableForm.stream().filter(t -> key.equals(t.getChildList().getTableModel())).findFirst().orElse(null);
                if (childModel != null) {
                    //子表主键
                    List<FormColumnModel> childList = childModel.getChildList().getChildList();
                    List<Map<String, Object>> childDataMap = (List<Map<String, Object>>) dataNewMap.get(key);
                    //子表处理的数据
                    List<Map<String, Object>> childResult = new ArrayList<>();
                    for (Map<String, Object> objectMap : childDataMap) {
                        //子表单体处理的数据
                        Map<String, Object> childOneResult = new HashMap<>(16);
                        for (String childKey : objectMap.keySet()) {
                            FormColumnModel columnModel = childList.stream().filter(t -> childKey.equals(t.getFieLdsModel().getVModel())).findFirst().orElse(null);
                            if (columnModel != null) {
                                FieLdsModel fieLdsModel = columnModel.getFieLdsModel();
                                Object data = objectMap.get(childKey);
                                //处理系统自动生成
                                data = this.create(fieLdsModel, data, false);
                                childOneResult.put(childKey, data);
                            }
                        }
                        childResult.add(childOneResult);
                    }
                    result.put(key, childResult);
                }
            }
        }
        return result;
    }

    //--------------------------------------------修改 ----------------------------------------------------

    /**
     * 修改数据处理
     **/
    public Map<String, Object> update(Map<String, Object> allDataMap, List<FieLdsModel> fieLdsModelList, List<TableModel> tableModelList, String mainId, DbLinkEntity link) throws WorkFlowException {
        DataModel dataModel = new DataModel(allDataMap, fieLdsModelList, tableModelList, mainId, link, false);
        Map<String, Object> result = this.update(dataModel);
        return result;
    }

    /**
     * 修改数据处理
     **/
    public Map<String, Object> update(DataModel dataModel) throws WorkFlowException {
        try {
            List<FieLdsModel> fieLdsModelList = dataModel.getFieLdsModelList();
            List<TableModel> tableModelList = dataModel.getTableModelList();
            RecursionForm recursionForm = new RecursionForm(fieLdsModelList, tableModelList);
            List<FormAllModel> formAllModel = new ArrayList<>();
            //递归遍历模板
            FormCloumnUtil.recursionForm(recursionForm, formAllModel);
            //处理好的数据
            Map<String, Object> result = this.updateDataList(dataModel, formAllModel);
            return result;
        } catch (Exception e) {
            log.error("修改异常：{}", e.getMessage());
            throw new WorkFlowException(e.getMessage());
        }
    }

    /**
     * 修改数据
     **/
    private Map<String, Object> updateDataList(DataModel dataModel, List<FormAllModel> formAllModel) throws SQLException {
        //处理好的数据
        Map<String, Object> result = new HashMap<>(16);
        String mainId = dataModel.getMainId();
        Map<String, Object> dataNewMap = dataModel.getDataNewMap();
        List<TableModel> tableModelList = dataModel.getTableModelList();
        //有表数据处理
        if (tableModelList.size() > 0) {
            DbLinkEntity link = dataModel.getLink();
            boolean isOracle = (DbTypeUtil.checkOracle(dataSourceUtil) || DbTypeUtil.checkPostgre(dataSourceUtil));
            if (link != null) {
                isOracle = (DbTypeUtil.checkOracle(link) || DbTypeUtil.checkPostgre(link));
            }
            //系统数据
            @Cleanup Connection conn = this.getTableConn(link);
            conn.setAutoCommit(false);
            //子表
            this.updateTable(formAllModel, tableModelList, isOracle, dataNewMap, conn, mainId, result);
            //副表
            this.updateMastTable(formAllModel, tableModelList, isOracle, dataNewMap, conn, mainId, result);
            //主表
            this.updateMast(formAllModel, tableModelList, isOracle, dataNewMap, conn, mainId, result);
        } else {
            //无表数据处理
            result = this.updateAll(dataNewMap, formAllModel);
        }
        return result;
    }

    /**
     * 子表数据
     **/
    private void updateTable(List<FormAllModel> formAllModel, List<TableModel> tableModelList, boolean isOracle, Map<String, Object> dataNewMap, Connection conn, String mainId, Map<String, Object> result) throws SQLException {
        //子表
        List<FormAllModel> tableForm = formAllModel.stream().filter(t -> FormEnum.table.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
        Map<String, List<FormColumnModel>> childMap = new HashMap<>();
        Map<String, TableModel> chidTable = new HashMap<>();
        tableForm.stream().forEach(t -> {
            FormColumnTableModel childListAll = t.getChildList();
            String tableModel = childListAll.getTableModel();
            List<FormColumnModel> childList = childListAll.getChildList().stream().filter(g -> StringUtil.isNotEmpty(g.getFieLdsModel().getVModel())).collect(Collectors.toList());
            childMap.put(tableModel, childList);
            String tableName = childListAll.getTableName();
            Optional<TableModel> first = tableModelList.stream().filter(k -> k.getTable().equals(tableName)).findFirst();
            if (first.isPresent()) {
                TableModel childTable = first.get();
                chidTable.put(tableModel, childTable);
            }
        });
        for (String key : childMap.keySet()) {
            //子表数据
            List<Map<String, Object>> chidList = dataNewMap.get(key) != null ? (List<Map<String, Object>>) dataNewMap.get(key) : new ArrayList<>();
            List<FormColumnModel> formColumnModels = childMap.get(key);
            Map<String, FieLdsModel> columMap = new HashMap<>();
            //获取子表对象的类型
            for (FormColumnModel column : formColumnModels) {
                FieLdsModel fieLdsModel = column.getFieLdsModel();
                String vmodel = fieLdsModel.getVModel();
                columMap.put(vmodel, fieLdsModel);
            }
            //子表主键
            TableModel tableModel = chidTable.get(key);
            String table = tableModel.getTable();
            String childKeyName = this.getKey(conn, table);
            //关联字段
            Optional<TableModel> first = tableModelList.stream().filter(t -> t.getTable().equals(table)).findFirst();
            if (!first.isPresent()) {
                throw new SQLException(MsgCode.COD001.get());
            }
            String mastKeyName = first.get().getTableField();
            //sql组装
            List<List<Object>> childDataAll = new ArrayList<>();
            List<Map<String, Object>> childResultAll = new ArrayList<>();
            StringJoiner filedModel = new StringJoiner(",");
            StringJoiner filedValue = new StringJoiner(",");
            //添加字段只要保存一次
            boolean isFile = true;
            for (Map<String, Object> objectMap : chidList) {
                List<Object> childData = new ArrayList<>();
                Map<String, Object> childOneResult = new HashMap<>(16);
                for (String childKey : columMap.keySet()) {
                    FieLdsModel fieLdsModel = columMap.get(childKey);
                    String jnpfkey = fieLdsModel.getConfig().getJnpfKey();
                    Object data = objectMap.get(childKey);
                    //处理系统自动生成
                    data = this.update(fieLdsModel, data, true);
                    String value = (isOracle && (JnpfKeyConsts.DATE.equals(jnpfkey) || JnpfKeyConsts.MODIFYTIME.equals(jnpfkey) || JnpfKeyConsts.CREATETIME.equals(jnpfkey))) ? "to_date(?,'yyyy-mm-dd HH24:mi:ss')" : "?";
                    //添加数据
                    childData.add(data);
                    childOneResult.put(childKey, data);
                    //添加字段
                    if (isFile) {
                        filedModel.add(childKey);
                        filedValue.add(value);
                    }
                }
                //添加主键值和外键值
                childData.add(RandomUtil.uuId());
                childData.add(mainId);
                //添加主键和外键
                if (isFile) {
                    //sql主键
                    filedModel.add(childKeyName);
                    filedValue.add("?");
                    //关联字段
                    filedModel.add(mastKeyName);
                    filedValue.add("?");
                }
                isFile = false;
                //保存list值
                childResultAll.add(childOneResult);
                childDataAll.add(childData);
            }
            //返回值
            result.put(key, childResultAll);
            String[] del = new String[]{"delete from " + table + " where " + mastKeyName + " = ?", mainId};
            String childSql = "insert into " + table + "(" + filedModel + ") values (" + filedValue + ")";
            this.sql(childSql, childDataAll, del, conn, false);
        }
    }

    /**
     * 副表数据
     **/
    private void updateMastTable(List<FormAllModel> formAllModel, List<TableModel> tableModelList, boolean isOracle, Map<String, Object> dataNewMap, Connection conn, String mainId, Map<String, Object> result) throws SQLException {
        //副表
        Map<String, List<FormAllModel>> mastTableAll = formAllModel.stream().filter(t -> FormEnum.mastTable.getMessage().equals(t.getJnpfKey())).collect(Collectors.groupingBy(e -> e.getFormMastTableModel().getTable()));
        for (String key : mastTableAll.keySet()) {
            //副表
            Optional<TableModel> first = tableModelList.stream().filter(t -> t.getTable().equals(key)).findFirst();
            if (!first.isPresent()) {
                throw new SQLException(MsgCode.COD001.get());
            }
            TableModel tableModel = first.get();
            String tableModelTable = tableModel.getTable();
            String childKeyName = this.getKey(conn, tableModelTable);
            //关联字段
            String mastKeyName = tableModel.getTableField();
            List<FormAllModel> masTableList = mastTableAll.get(key);
            //修改字段
            StringJoiner filed = new StringJoiner(",");
            List<Object> mastData = new LinkedList<>();
            for (FormAllModel model : masTableList) {
                FormMastTableModel formMastTableModel = model.getFormMastTableModel();
                FormColumnModel mastTable = formMastTableModel.getMastTable();
                FieLdsModel fieLdsModel = mastTable.getFieLdsModel();
                String mostTableKey = fieLdsModel.getVModel();
                String jnpfkey = fieLdsModel.getConfig().getJnpfKey();
                Object data = dataNewMap.get(mostTableKey);
                //处理系统自动生成
                data = this.update(fieLdsModel, data, true);
                //返回值
                result.put(mostTableKey, data);
                //添加字段
                mastData.add(data);
                String field = formMastTableModel.getField();
                String value = (isOracle && (JnpfKeyConsts.DATE.equals(jnpfkey) || JnpfKeyConsts.MODIFYTIME.equals(jnpfkey) || JnpfKeyConsts.CREATETIME.equals(jnpfkey))) ? "to_date(?,'yyyy-mm-dd HH24:mi:ss')" : "?";
                filed.add(field + "=" + value);
            }
            //sql主键
            mastData.add(RandomUtil.uuId());
            filed.add(childKeyName + "= ?");
            //关联字段
            mastData.add(mainId);
            //新增sql语句
            String sql = "update " + tableModelTable + " set " + filed + " where " + mastKeyName + "= ?";
            List<List<Object>> data = new LinkedList<>();
            data.add(mastData);
            this.sql(sql, data, new String[]{}, conn, false);
        }
    }

    /**
     * 主表数据
     **/
    private void updateMast(List<FormAllModel> formAllModel, List<TableModel> tableModelList, boolean isOracle, Map<String, Object> dataNewMap, Connection conn, String mainId, Map<String, Object> result) throws SQLException {
        Optional<TableModel> first = tableModelList.stream().filter(t -> "1".equals(t.getTypeId())).findFirst();
        if (!first.isPresent()) {
            throw new SQLException(MsgCode.COD001.get());
        }
        TableModel tableModel = first.get();
        String mastTableName = tableModel.getTable();
        List<FormAllModel> mastForm = formAllModel.stream().filter(t -> FormEnum.mast.getMessage().equals(t.getJnpfKey())).filter(t -> StringUtil.isNotEmpty(t.getFormColumnModel().getFieLdsModel().getVModel())).collect(Collectors.toList());
        //修改字段
        StringJoiner filed = new StringJoiner(",");
        List<Object> mastData = new LinkedList<>();
        String keyName = this.getKey(conn, mastTableName);
        for (FormAllModel model : mastForm) {
            FieLdsModel fieLdsModel = model.getFormColumnModel().getFieLdsModel();
            String jnpfkey = fieLdsModel.getConfig().getJnpfKey();
            String field = fieLdsModel.getVModel();
            Object data = dataNewMap.get(field);
            //处理系统自动生成
            data = this.update(fieLdsModel, data, true);
            mastData.add(data);
            //添加字段
            String value = (isOracle && (JnpfKeyConsts.DATE.equals(jnpfkey) || JnpfKeyConsts.MODIFYTIME.equals(jnpfkey) || JnpfKeyConsts.CREATETIME.equals(jnpfkey))) ? "to_date(?,'yyyy-mm-dd HH24:mi:ss')" : "?";
            filed.add(field + "=" + value);
            result.put(field, data);
        }
        //sql主键
        mastData.add(mainId);
        //修改sql语句
        String sql = "update " + mastTableName + " set " + filed + " where " + keyName + "= ?";
        List<List<Object>> data = new LinkedList<>();
        data.add(mastData);
        this.sql(sql, data, new String[]{}, conn, true);
    }

    /**
     * 修改无表数据
     **/
    private Map<String, Object> updateAll(Map<String, Object> dataNewMap, List<FormAllModel> formAllModel) {
        //处理好的数据
        Map<String, Object> result = new HashMap<>(16);
        //系统数据
        List<FormAllModel> mastForm = formAllModel.stream().filter(t -> FormEnum.mast.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
        List<FormAllModel> tableForm = formAllModel.stream().filter(t -> FormEnum.table.getMessage().equals(t.getJnpfKey())).collect(Collectors.toList());
        for (String key : dataNewMap.keySet()) {
            FormAllModel model = mastForm.stream().filter(t -> key.equals(t.getFormColumnModel().getFieLdsModel().getVModel())).findFirst().orElse(null);
            if (model != null) {
                FieLdsModel fieLdsModel = model.getFormColumnModel().getFieLdsModel();
                Object data = dataNewMap.get(key);
                //处理系统自动生成
                data = this.update(fieLdsModel, data, false);
                result.put(key, data);
            } else {
                FormAllModel childModel = tableForm.stream().filter(t -> key.equals(t.getChildList().getTableModel())).findFirst().orElse(null);
                if (childModel != null) {
                    List<Map<String, Object>> childDataMap = (List<Map<String, Object>>) dataNewMap.get(key);
                    //子表处理的数据
                    List<Map<String, Object>> childResult = new ArrayList<>();
                    for (Map<String, Object> objectMap : childDataMap) {
                        //子表单体处理的数据
                        Map<String, Object> childOneResult = new HashMap<>(16);
                        for (String childKey : objectMap.keySet()) {
                            FormColumnModel columnModel = childModel.getChildList().getChildList().stream().filter(t -> childKey.equals(t.getFieLdsModel().getVModel())).findFirst().orElse(null);
                            if (columnModel != null) {
                                FieLdsModel fieLdsModel = columnModel.getFieLdsModel();
                                Object data = objectMap.get(childKey);
                                data = this.update(fieLdsModel, data, false);
                                childOneResult.put(childKey, data);
                            }
                        }
                        childResult.add(childOneResult);
                    }
                    result.put(key, childResult);
                }
            }
        }
        return result;
    }

    /**
     * 修改系统赋值
     **/
    private Object update(FieLdsModel fieLdsModel, Object dataValue, boolean isTable) {
        String jnpfKey = fieLdsModel.getConfig().getJnpfKey();
        String rule = fieLdsModel.getConfig().getRule();
        UserInfo userInfo = userProvider.get();
        Object value = dataValue;
        switch (jnpfKey) {
            case JnpfKeyConsts.CREATEUSER:
                if (!ObjectUtil.isEmpty(value)) {
                    UserEntity userEntity = serviceUtil.getByRealName(String.valueOf(value));
                    value = userEntity != null ? userEntity.getId() : userInfo.getUserId();
                } else {
                    value = userInfo.getUserId();
                }
                break;
            case JnpfKeyConsts.CREATETIME:
                if (ObjectUtil.isEmpty(value)) {
                    value = DateUtil.getNow("+8");
                }
                break;
            case JnpfKeyConsts.CURRORGANIZE:
            case JnpfKeyConsts.CURRDEPT:
                if (!ObjectUtil.isEmpty(value)) {
                    String posValue = String.valueOf(value);
                    //多级组织取最后一级
                    if (posValue.contains("/")) {
                        posValue = posValue.substring(posValue.lastIndexOf("/") + 1);
                    }
                    OrganizeEntity organizeEntity = serviceUtil.getOrganizeFullName(posValue);
                    value = organizeEntity != null ? organizeEntity.getId() : value;
                }
                break;
            case JnpfKeyConsts.MODIFYTIME:
                value = DateUtil.getNow("+8");
                break;
            case JnpfKeyConsts.MODIFYUSER:
                value = userInfo.getUserId();
                break;
            case JnpfKeyConsts.CURRPOSITION:
                if (!ObjectUtil.isEmpty(value)) {
                    PositionEntity positionEntity = serviceUtil.getPositionFullName(String.valueOf(value));
                    value = positionEntity != null ? positionEntity.getId() : "";
                }
                break;
            case JnpfKeyConsts.BILLRULE:
                if (ObjectUtil.isEmpty(value)) {
                    try {
                        value = serviceUtil.getBillNumber(rule);
                    } catch (Exception e) {
                        value = null;
                    }
                }
                break;
            case JnpfKeyConsts.DATE:
                if (isTable) {
                    try {
                        value = DateUtil.dateToString(new Date(Long.valueOf(String.valueOf(dataValue))), "yyyy-MM-dd HH:mm:ss");
                    } catch (Exception e) {

                    }
                }
                break;
            case JnpfKeyConsts.NUM_INPUT:
                if (isTable) {
                    try {
                        value = new BigDecimal(String.valueOf(dataValue));
                    } catch (Exception e) {

                    }
                }
                break;
            default:
                if (isTable) {
                    if (value instanceof List) {
                        value = String.valueOf(value);
                    }
                }
                break;
        }
        return value;
    }

    /**
     * 执行sql语句
     **/
    private void sql(String sql, List<List<Object>> dataAll, String[] del, Connection conn, boolean isCommit) throws SQLException {
        try {
            if (del.length > 0) {
                @Cleanup PreparedStatement delete = conn.prepareStatement(del[0]);
                delete.setObject(1, del[1]);
                delete.addBatch();
                delete.executeBatch();
            }
            @Cleanup PreparedStatement save = conn.prepareStatement(sql);
            for (List<Object> childData : dataAll) {
                for (int i = 0; i < childData.size(); i++) {
                    Object data = childData.get(i);
                    save.setObject(i + 1, data);
                }
                save.addBatch();
                save.executeBatch();
            }
            if (isCommit) {
                conn.commit();
            }
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("sql语句异常：" + e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

}
