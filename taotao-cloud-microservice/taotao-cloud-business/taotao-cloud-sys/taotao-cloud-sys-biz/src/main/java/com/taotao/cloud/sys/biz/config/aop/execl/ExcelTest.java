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

package com.taotao.cloud.sys.biz.config.aop.execl;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.taotao.boot.common.utils.log.LogUtils;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;

/**
 * ExcelTest easyExcel大数量批次导入、导出测试
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-10 09:10
 */
public class ExcelTest {

    // 导出逻辑代码
    public void dataExport300w( HttpServletResponse response ) {
        {
            OutputStream outputStream = null;
            try {
                long startTime = System.currentTimeMillis();
                LogUtils.info("导出开始时间:" + startTime);

                outputStream = response.getOutputStream();
                WriteWorkbook writeWorkbook = new WriteWorkbook();
                writeWorkbook.setOutputStream(outputStream);
                writeWorkbook.setExcelType(ExcelTypeEnum.XLSX);
                ExcelWriter writer = new ExcelWriter(writeWorkbook);
                String fileName = new String(( "excel100w" ).getBytes(), StandardCharsets.UTF_8);

                // title
                WriteTable table = new WriteTable();
                table.setTableNo(1);
                List<List<String>> titles = new ArrayList<>();
                titles.add(List.of("onlineseqid"));
                titles.add(List.of("businessid"));
                titles.add(List.of("becifno"));
                titles.add(List.of("ivisresult"));
                titles.add(List.of("createdby"));
                titles.add(List.of("createddate"));
                titles.add(List.of("updateby"));
                titles.add(List.of("updateddate"));
                titles.add(List.of("risklevel"));
                table.setHead(titles);

                // 模拟统计查询的数据数量这里模拟100w
                int count = 3000001;
                // 记录总数:实际中需要根据查询条件进行统计即可
                // Integer totalCount = actResultLogMapper.findActResultLogByCondations(count);
                Integer totalCount = 300 * 10000;
                // 每一个Sheet存放100w条数据
                Integer sheetDataRows = 100 * 10000;
                // 每次写入的数据量20w
                Integer writeDataRows = 20 * 10000;
                // 计算需要的Sheet数量
                int sheetNum = totalCount % sheetDataRows == 0
                        ? ( totalCount / sheetDataRows )
                        : ( totalCount / sheetDataRows + 1 );
                // 计算一般情况下每一个Sheet需要写入的次数(一般情况不包含最后一个sheet,因为最后一个sheet不确定会写入多少条数据)
                int oneSheetWriteCount = sheetDataRows / writeDataRows;
                // 计算最后一个sheet需要写入的次数
                int lastSheetWriteCount = totalCount % sheetDataRows == 0
                        ? oneSheetWriteCount
                        : ( totalCount % sheetDataRows % writeDataRows == 0
                                ? ( totalCount / sheetDataRows / writeDataRows )
                                : ( totalCount / sheetDataRows / writeDataRows + 1 ) );

                // 开始分批查询分次写入
                // 注意这次的循环就需要进行嵌套循环了,外层循环是Sheet数目,内层循环是写入次数
                List<List<String>> dataList = new ArrayList<>();
                for (int i = 0; i < sheetNum; i++) {
                    // 创建Sheet
                    WriteSheet sheet = new WriteSheet();
                    sheet.setSheetNo(i);
                    sheet.setSheetName("测试Sheet1" + i);

                    // 循环写入次数:
                    // j的自增条件是当不是最后一个Sheet的时候写入次数为正常的每个Sheet写入的次数,如果是最后一个就需要使用计算的次数lastSheetWriteCount
                    for (int j = 0; j < ( i != sheetNum - 1 ? oneSheetWriteCount : lastSheetWriteCount ); j++) {
                        // 集合复用,便于GC清理
                        dataList.clear();
                        // 分页查询一次20w
                        // PageHelper.startPage(j + 1 + oneSheetWriteCount * i, writeDataRows);
                        // List<ActResultLog> reslultList = actResultLogMapper.findByPage100w();
                        // if (!CollectionUtils.isEmpty(reslultList)) {
                        //	reslultList.forEach(item -> {
                        //		dataList.add(
                        //			Arrays.asList(item.getOnlineseqid(), item.getBusinessid(),
                        //				item.getBecifno(), item.getIvisresult(),
                        //				item.getCreatedby(),
                        //				Calendar.getInstance().getTime().toString(),
                        //				item.getUpdateby(),
                        //				Calendar.getInstance().getTime().toString(),
                        //				item.getRisklevel()));
                        //	});
                        // }
                        // 写数据
                        writer.write(dataList, sheet, table);
                    }
                }

                // 下载EXCEL
                response.setHeader(
                        "Content-Disposition",
                        "attachment;filename=" + new String(( fileName ).getBytes("gb2312"), "ISO-8859-1") + ".xlsx");
                response.setContentType("multipart/form-data");
                response.setCharacterEncoding("utf-8");
                writer.finish();
                outputStream.flush();
                // 导出时间结束
                long endTime = System.currentTimeMillis();
                LogUtils.info("导出结束时间:" + endTime + "ms");
                LogUtils.info("导出所用时间:" + ( endTime - startTime ) / 1000 + "秒");
            } catch (FileNotFoundException e) {
                LogUtils.error(e);
            } catch (IOException e) {
                LogUtils.error(e);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        LogUtils.error(e);
                    }
                }
            }
        }
    }

    public void import2DBFromExcel10wTest() {
        String fileName =
                "D:\\StudyWorkspace\\JavaWorkspace\\java_project_workspace\\idea_projects\\SpringBootProjects\\easyexcel\\exportFile\\excel300w.xlsx";
        // 记录开始读取Excel时间,也是导入程序开始时间
        long startReadTime = System.currentTimeMillis();
        LogUtils.info("------开始读取Excel的Sheet时间(包括导入数据过程):" + startReadTime + "ms------");
        // 读取所有Sheet的数据.每次读完一个Sheet就会调用这个方法
        // EasyExcel.read(fileName, new EasyExceGeneralDatalListener(actResultLogService2))
        //	.doReadAll();
        long endReadTime = System.currentTimeMillis();
        LogUtils.info("------结束读取Excel的Sheet时间(包括导入数据过程):" + endReadTime + "ms------");
    }

    // 事件监听
    /**
     * EasyExceGeneralDatalListener
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class EasyExceGeneralDatalListener extends AnalysisEventListener<Map<Integer, String>> {

        /**
         * 用于存储读取的数据
         */
        private List<Map<Integer, String>> dataList = new ArrayList<Map<Integer, String>>();

        public EasyExceGeneralDatalListener() {
        }

        @Override
        public void invoke( Map<Integer, String> data, AnalysisContext context ) {
            // 数据add进入集合
            dataList.add(data);
            // size是否为100000条:这里其实就是分批.当数据等于10w的时候执行一次插入
            if (dataList.size() >= 10 * 10000) {
                // 存入数据库:数据小于1w条使用Mybatis的批量插入即可;
                saveData();
                // 清理集合便于GC回收
                dataList.clear();
            }
        }

        /**
         * 保存数据到DB
         *
         * @param @MethodName: saveData
         * @return: void
         */
        private void saveData() {
            // actResultLogService2.import2DBFromExcel10w(dataList);
            dataList.clear();
        }

        /**
         * Excel中所有数据解析完毕会调用此方法
         *
         * @param: context @MethodName: doAfterAllAnalysed
         * @return: void
         */
        @Override
        public void doAfterAllAnalysed( AnalysisContext context ) {
            saveData();
            dataList.clear();
        }
    }

    // JDBC工具类
    /**
     * JDBCDruidUtils
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class JDBCDruidUtils {

        private static DataSource dataSource;

        /*
          创建数据Properties集合对象加载加载配置文件
        */
        static {
            Properties pro = new Properties();
            // 加载数据库连接池对象
            try {
                // 获取数据库连接池对象
                pro.load(JDBCDruidUtils.class.getClassLoader().getResourceAsStream("druid.properties"));
                dataSource = DruidDataSourceFactory.createDataSource(pro);
            } catch (Exception e) {
                LogUtils.error(e);
            }
        }

        /*
        获取连接
         */
        public static Connection getConnection() throws SQLException {
            return dataSource.getConnection();
        }

        /**
         * 关闭conn,和 statement独对象资源
         *
         * @param statement @MethodName: close
         * @return: void
         */
        public static void close( Connection connection, Statement statement ) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LogUtils.error(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    LogUtils.error(e);
                }
            }
        }

        /**
         * 关闭 conn , statement 和resultset三个对象资源
         *
         * @param resultSet @MethodName: close
         * @return: void
         */
        public static void close( Connection connection, Statement statement, ResultSet resultSet ) {
            close(connection, statement);
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    LogUtils.error(e);
                }
            }
        }

        /*
        获取连接池对象
         */
        public static DataSource getDataSource() {
            return dataSource;
        }
    }

    // Service中具体业务逻辑

    /**
     * 测试用Excel导入超过10w条数据,经过测试发现,使用Mybatis的批量插入速度非常慢,所以这里可以使用 数据分批+JDBC分批插入+事务来继续插入速度会非常快
     *
     * @param @MethodName: import2DBFromExcel10w
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    public Map<String, Object> import2DBFromExcel10w( List<Map<Integer, String>> dataList ) {
        HashMap<String, Object> result = new HashMap<>();
        // 结果集中数据为0时,结束方法.进行下一次调用
        if (dataList.size() == 0) {
            result.put("empty", "0000");
            return result;
        }
        // JDBC分批插入+事务操作完成对10w数据的插入
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long startTime = System.currentTimeMillis();
            LogUtils.info(dataList.size() + "条,开始导入到数据库时间:" + startTime + "ms");
            conn = JDBCDruidUtils.getConnection();
            // 控制事务:默认不提交
            conn.setAutoCommit(false);
            String sql = "insert into ACT_RESULT_LOG"
                    + " (onlineseqid,businessid,becifno,ivisresult,createdby,createddate,updateby,updateddate,risklevel)"
                    + " values";
            sql += "(?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            // 循环结果集:这里循环不支持"烂布袋"表达式
            for (int i = 0; i < dataList.size(); i++) {
                Map<Integer, String> item = dataList.get(i);
                ps.setString(1, item.get(0));
                ps.setString(2, item.get(1));
                ps.setString(3, item.get(2));
                ps.setString(4, item.get(3));
                ps.setString(5, item.get(4));
                ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                ps.setString(7, item.get(6));
                ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                ps.setString(9, item.get(8));
                // 将一组参数添加到此 PreparedStatement 对象的批处理命令中。
                ps.addBatch();
            }
            // 执行批处理
            ps.executeBatch();
            // 手动提交事务
            conn.commit();
            long endTime = System.currentTimeMillis();
            LogUtils.info(dataList.size() + "条,结束导入到数据库时间:" + endTime + "ms");
            LogUtils.info(dataList.size() + "条,导入用时:" + ( endTime - startTime ) + "ms");
            result.put("success", "1111");
        } catch (Exception e) {
            result.put("exception", "0000");
            LogUtils.error(e);
        } finally {
            // 关连接
            JDBCDruidUtils.close(conn, ps);
        }
        return result;
    }
}
