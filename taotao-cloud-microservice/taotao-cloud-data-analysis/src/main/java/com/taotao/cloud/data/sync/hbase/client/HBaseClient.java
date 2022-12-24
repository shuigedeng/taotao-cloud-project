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
package com.taotao.cloud.data.sync.hbase.client;

import com.taotao.cloud.bigdata.hbase.configuration.HbaseAutoConfiguration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * HBaseClient
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/30 11:32
 */
@Slf4j
@Component
@DependsOn("hbaseConfig")
public class HBaseClient {

	@Autowired
	private HbaseAutoConfiguration config;
	private static Admin admin = null;
	public static Configuration conf = null;
	private static Connection connection = null;
	private ThreadLocal<List<Put>> threadLocal = new ThreadLocal<List<Put>>();
	private static final int CACHE_LIST_SIZE = 1000;

	@PostConstruct
	private void init() {
		if (connection != null) {
			return;
		}

		try {
			connection = ConnectionFactory.createConnection(config.configuration());
			admin = connection.getAdmin();
		} catch (IOException e) {
			log.error("HBase create connection failed: {}", e);
		}
	}

	/**
	 * 创建表 experssion : create 'tableName','[Column Family 1]','[Column Family 2]'
	 *
	 * @param tableName      表名
	 * @param columnFamilies 列族名
	 * @throws IOException
	 */
	public void createTable(String tableName, String... columnFamilies) throws IOException {
		TableName name = TableName.valueOf(tableName);
		boolean isExists = this.tableExists(tableName);

		if (isExists) {
			throw new TableExistsException(tableName + "is exists!");
		}

		TableDescriptorBuilder descriptorBuilder = TableDescriptorBuilder.newBuilder(name);
		List<ColumnFamilyDescriptor> columnFamilyList = new ArrayList<>();

		for (String columnFamily : columnFamilies) {
			ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder
					.newBuilder(columnFamily.getBytes()).build();
			columnFamilyList.add(columnFamilyDescriptor);
		}

		descriptorBuilder.setColumnFamilies(columnFamilyList);
		TableDescriptor tableDescriptor = descriptorBuilder.build();
		admin.createTable(tableDescriptor);
	}

	/**
	 * 插入或更新 experssion : put <tableName>,<rowKey>,<family:column>,<value>,<timestamp>
	 *
	 * @param tableName    表名
	 * @param rowKey       行id
	 * @param columnFamily 列族名
	 * @param column       列
	 * @param value        值
	 * @throws IOException
	 */
	public void insertOrUpdate(String tableName, String rowKey, String columnFamily, String column,
			String value)
			throws IOException {
		this.insertOrUpdate(tableName, rowKey, columnFamily, new String[]{column},
				new String[]{value});
	}

	/**
	 * 插入或更新多个字段 experssion : put <tableName>,<rowKey>,<family:column>,<value>,<timestamp>
	 *
	 * @param tableName    表名
	 * @param rowKey       行id
	 * @param columnFamily 列族名
	 * @param columns      列
	 * @param values       值
	 * @throws IOException
	 */
	public void insertOrUpdate(String tableName, String rowKey, String columnFamily,
			String[] columns, String[] values)
			throws IOException {
		Table table = connection.getTable(TableName.valueOf(tableName));

		Put put = new Put(Bytes.toBytes(rowKey));

		for (int i = 0; i < columns.length; i++) {
			put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columns[i]),
					Bytes.toBytes(values[i]));
			table.put(put);
		}
	}

	/**
	 * 删除行
	 *
	 * @param tableName 表名
	 * @param rowKey    行id
	 * @throws IOException
	 */
	public void deleteRow(String tableName, String rowKey) throws IOException {
		Table table = connection.getTable(TableName.valueOf(tableName));

		Delete delete = new Delete(rowKey.getBytes());

		table.delete(delete);
	}

	/**
	 * 删除列族
	 *
	 * @param tableName    表名
	 * @param rowKey       行id
	 * @param columnFamily 列族名
	 * @throws IOException
	 */
	public void deleteColumnFamily(String tableName, String rowKey, String columnFamily)
			throws IOException {
		Table table = connection.getTable(TableName.valueOf(tableName));

		Delete delete = new Delete(rowKey.getBytes());
		delete.addFamily(Bytes.toBytes(columnFamily));

		table.delete(delete);
	}

	/**
	 * 删除列 experssion : delete 'tableName','rowKey','columnFamily:column'
	 *
	 * @param tableName    表名
	 * @param rowKey       行id
	 * @param columnFamily 列族名
	 * @param column       列名
	 * @throws IOException
	 */
	public void deleteColumn(String tableName, String rowKey, String columnFamily, String column)
			throws IOException {
		Table table = connection.getTable(TableName.valueOf(tableName));

		Delete delete = new Delete(rowKey.getBytes());
		delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));

		table.delete(delete);
	}

	/**
	 * 删除表 experssion : disable 'tableName' 之后 drop 'tableName'
	 *
	 * @param tableName 表名
	 * @throws IOException
	 */
	public void deleteTable(String tableName) throws IOException {
		boolean isExists = this.tableExists(tableName);

		if (!isExists) {
			return;
		}

		TableName name = TableName.valueOf(tableName);
		admin.disableTable(name);
		admin.deleteTable(name);
	}

	/**
	 * 获取值 experssion : get 'tableName','rowkey','family:column'
	 *
	 * @param tableName 表名
	 * @param rowkey    行id
	 * @param family    列族名
	 * @param column    列名
	 * @return
	 */
	public String getValue(String tableName, String rowkey, String family, String column) {
		Table table = null;
		String value = "";

		if (StringUtils.isBlank(tableName) || StringUtils.isBlank(family) || StringUtils
				.isBlank(rowkey) || StringUtils
				.isBlank(column)) {
			return null;
		}

		try {
			table = connection.getTable(TableName.valueOf(tableName));
			Get g = new Get(rowkey.getBytes());
			g.addColumn(family.getBytes(), column.getBytes());
			Result result = table.get(g);
			List<Cell> ceList = result.listCells();
			if (ceList != null && ceList.size() > 0) {
				for (Cell cell : ceList) {
					value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
							cell.getValueLength());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * 查询指定行 experssion : get 'tableName','rowKey'
	 *
	 * @param tableName 表名
	 * @param rowKey    行id
	 * @return
	 * @throws IOException
	 */
	public String selectOneRow(String tableName, String rowKey) throws IOException {
		Table table = connection.getTable(TableName.valueOf(tableName));
		Get get = new Get(rowKey.getBytes());
		Result result = table.get(get);
		NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = result
				.getMap();

		for (Cell cell : result.rawCells()) {
			String row = Bytes.toString(cell.getRowArray());
			String columnFamily = Bytes.toString(cell.getFamilyArray());
			String column = Bytes.toString(cell.getQualifierArray());
			String value = Bytes.toString(cell.getValueArray());
			// 可以通过反射封装成对象(列名和Java属性保持一致)
			System.out.println(row);
			System.out.println(columnFamily);
			System.out.println(column);
			System.out.println(value);
		}
		return null;
	}


	/**
	 * 根据条件取出点位指定时间内的所有记录
	 *
	 * @param tableName   表名("OPC_TEST")
	 * @param family      列簇名("OPC_COLUMNS")
	 * @param column      列名("site")
	 * @param value       值(采集点标识)
	 * @param startMillis 开始时间毫秒值(建议传递当前时间前一小时的毫秒值，在保证查询效率的前提下获取到点位最新的记录)
	 * @param endMMillis  结束时间毫秒值(当前时间)
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	public Map<String, String> scanBatchOfTable(String tableName, String family, String[] column,
			String[] value, Long startMillis, Long endMillis) throws IOException {

		if (Objects.isNull(column) || Objects.isNull(column) || column.length != value.length) {
			return null;
		}

		FilterList filterList = new FilterList();

		for (int i = 0; i < column.length; i++) {
			SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family),
					Bytes.toBytes(column[i]), CompareOperator.EQUAL, Bytes.toBytes(value[i]));
			filterList.addFilter(filter);
		}

		Table table = connection.getTable(TableName.valueOf(tableName));

		Scan scan = new Scan();
		scan.setFilter(filterList);

		if (startMillis != null && endMillis != null) {
			scan.setTimeRange(startMillis, endMillis);
		}

		ResultScanner scanner = table.getScanner(scan);
		Map<String, String> resultMap = new HashMap<>();

		try {
			for (Result result : scanner) {
				for (Cell cell : result.rawCells()) {
					String values = Bytes.toString(CellUtil.cloneValue(cell));
					String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));

					resultMap.put(qualifier, values);
				}
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			return resultMap;
		}
	}

	/**
	 * 根据条件取出点位最近时间的一条记录 experssion : scan 't1',{FILTER=>"PrefixFilter('2015')"}
	 *
	 * @param tableName   表名("OPC_TEST")
	 * @param family      列簇名("OPC_COLUMNS")
	 * @param column      列名("site")
	 * @param value       值(采集点标识)
	 * @param startMillis 开始时间毫秒值(建议传递当前时间前一小时的毫秒值，在保证查询效率的前提下获取到点位最新的记录)
	 * @param endMMillis  结束时间毫秒值(当前时间)
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	public Map<String, String> scanOneOfTable(String tableName, String family, String column,
			String value, Long startMillis, Long endMillis) throws IOException {
		Table table = connection.getTable(TableName.valueOf(tableName));

		Scan scan = new Scan();
		scan.setReversed(true);

		PageFilter pageFilter = new PageFilter(1); //
		scan.setFilter(pageFilter);

		if (startMillis != null && endMillis != null) {
			scan.setTimeRange(startMillis, endMillis);
		}

		if (StringUtils.isNotBlank(column)) {
			SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family),
					Bytes.toBytes(column), CompareOperator.EQUAL, Bytes.toBytes(value));
			scan.setFilter(filter);
		}

		ResultScanner scanner = table.getScanner(scan);
		Map<String, String> resultMap = new HashMap<>();

		try {
			for (Result result : scanner) {
				for (Cell cell : result.rawCells()) {
					String values = Bytes.toString(CellUtil.cloneValue(cell));
					String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));

					resultMap.put(qualifier, values);
				}
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			return resultMap;
		}
	}


	/**
	 * 判断表是否已经存在，这里使用间接的方式来实现
	 *
	 * @param tableName 表名
	 * @return
	 * @throws IOException
	 */
	public boolean tableExists(String tableName) throws IOException {
		TableName[] tableNames = admin.listTableNames();
		if (tableNames != null && tableNames.length > 0) {
			for (int i = 0; i < tableNames.length; i++) {
				if (tableName.equals(tableNames[i].getNameAsString())) {
					return true;
				}
			}
		}

		return false;
	}


	/**
	 * 批量添加
	 *
	 * @param tableName HBase表名
	 * @param rowkey    HBase表的rowkey
	 * @param cf        HBase表的columnFamily
	 * @param column    HBase表的列key
	 * @param values    写入HBase表的值value
	 * @param flag      提交标识符号。需要立即提交时，传递，值为 “end”
	 */
	public void bulkput(String tableName, String rowkey, String columnFamily, String[] columns,
			String[] values, String flag) {
		try {
			List<Put> list = threadLocal.get();
			if (list == null) {
				list = new ArrayList<Put>();
			}

			Put put = new Put(Bytes.toBytes(rowkey));

			for (int i = 0; i < columns.length; i++) {
				put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columns[i]),
						Bytes.toBytes(values[i]));
				list.add(put);
			}

			if (list.size() >= HBaseClient.CACHE_LIST_SIZE || flag.equals("end")) {

				Table table = connection.getTable(TableName.valueOf(tableName));
				table.put(list);

				list.clear();
			} else {
				threadLocal.set(list);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
