//package com.taotao.cloud.office.utils.easyexcel.other3;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 发薪单上传excel读取类
// *
// * <pre class="code">
// *         BatchReadListener listener = new BatchReadListener(dbFileBatchService, dbFileContentService, fileBatch);
// *     try {
// *         //注：headRowNumber默认为1，现赋值为2，即从第三行开始读取内容
// *         EasyExcel.read(fileInputStream, listener).headRowNumber(2).sheet().doRead();
// *     } catch (Exception e) {
// *         log.info("EasyExcel解析文件失败，{}", e);
// *         throw new CustomException("文件解析失败，请重新上传");
// *     }
// *     //获取表头信息进行处理
// *     Map<Integer, String> headTitleMap = listener.getHeadTitleMap();
// *     //获取动态表头信息
// *     List<String> headList = headTitleMap.keySet().stream().map(key -> {
// *         String head = headTitleMap.get(key);
// *         log.info(head);
// *         return head == null ? "" : head.replace("*", "");
// *     }).collect(Collectors.toList());
// *     //可以对表头进行入库保存，方便后续导出
// * </pre>
// *
// * @author yupf
// * @description Listener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
// */
//@Slf4j
//@Data
//public class BatchReadListener extends AnalysisEventListener<Map<Integer, String>> {
//
//	/**
//	 * 每隔500条存储数据库，然后清理list ，方便内存回收
//	 */
//	private static final int BATCH_COUNT = 500;
//	//Excel数据缓存结构
//	private List<Map<Integer, Map<Integer, String>>> list = new ArrayList<>();
//	//Excel表头（列名）数据缓存结构
//	private Map<Integer, String> headTitleMap = new HashMap<>();
//
//
//	/**
//	 * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
//	 */
//	private DbFileBatchService dbFileBatchService;
//	private DbFileContentService dbFileContentService;
//	private FileBatch fileBatch;
//	private int total = 0;
//
//	/**
//	 * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
//	 */
//	public BatchReadListener(DbFileBatchService dbFileBatchService,
//		DbFileContentService dbFileContentService, FileBatch fileBatch) {
//		this.dbFileBatchService = dbFileBatchService;
//		this.dbFileContentService = dbFileContentService;
//		this.fileBatch = fileBatch;
//	}
//
//	/**
//	 * 这个每一条数据解析都会来调用
//	 *
//	 * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
//	 * @param context
//	 */
//	@Override
//	public void invoke(Map<Integer, String> data, AnalysisContext context) {
//		log.info("解析到一条数据:{}", JSON.toJSONString(data));
//		total++;
//		Map<Integer, Map<Integer, String>> map = new HashMap<>();
//		map.put(context.readRowHolder().getRowIndex(), data);
//		list.add(map);
//		// 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//		if (list.size() >= BATCH_COUNT) {
//			saveData();
//			// 存储完成清理 list
//			list.clear();
//		}
//	}
//
//	/**
//	 * 所有数据解析完成了 都会来调用
//	 *
//	 * @param context
//	 */
//	@Override
//	public void doAfterAllAnalysed(AnalysisContext context) {
//		// 这里也要保存数据，确保最后遗留的数据也存储到数据库
//		saveData();
//		log.info("所有数据解析完成！");
//	}
//
//	/**
//	 * 解析表头数据
//	 **/
//	@Override
//	public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
//		log.info("表头数据：{}", JSONObject.toJSONString(headMap));
//		headTitleMap = headMap;
//	}
//
//	/**
//	 * 加上存储数据库
//	 */
//	private void saveData() {
//		log.info("{}条数据，开始存储数据库！", list.size());
//		FileContent fileContent = null;
//		List<FileContent> fileContentList = list.stream().flatMap(
//			integerMap -> integerMap.entrySet().stream().map(entrySet -> {
//					//entrySet.getKey()获取的是内容的RowIndex,实际的行数需要根据表头数进行处理
//					Integer rowIndex = entrySet.getKey();
//					Map<Integer, String> value = entrySet.getValue();
//					log.info(JSONObject.toJSONString(value));
//					fileContent = new FileContent();
//					fileContent.setBatchId(fileBatch.getId());
//					fileContent.setBatchNo(fileBatch.getBatchNo());
//					//固定字段入库
//					fileContent.setName(value.get(0) != null ? value.get(0).trim() : "");
//					fileContent.setCertNo(value.get(1) != null ? value.get(1).trim() : "");
//					fileContent.setRealAmount(value.get(2) != null ? value.get(2).trim() : "");
//					//所有动态表头数据转为JSON串入库
//					fileContent.setFieldsValue(JSONObject.toJSONString(value));
//					//取实际的内容rowIndex
//					fileContent.setRowNum(rowIndex + 1);
//					fileContent.setCreateTime(LocalDateTime.now());
//					return xcSalaryFileContent;
//				}
//			)).collect(Collectors.toList());
//		log.info(JSONObject.toJSONString(fileContentList));
//		dbFileContentService.saveBatch(fileContentList);
//		log.info("存储数据库成功！");
//	}
//
//	private List<List<String>> getFileHeadList(FileBatch fileBatch) {
//		String head = fileBatch.getFileHead();
//		List<String> headList = Arrays.asList(head.split(","));
//		List<List<String>> fileHead = headList.stream()
//			.map(item -> concatHead(Lists.newArrayList(item))).collect(Collectors.toList());
//		fileHead.add(concatHead(Lists.newArrayList("备注")));
//		return fileHead;
//	}
//
//	/**
//	 * 填写须知
//	 *
//	 * @param headContent
//	 * @return
//	 */
//	private List<String> concatHead(List<String> headContent) {
//		String remake =
//			"填写须知：                                                                                                \n"
//				+
//				"1.系统自动识别Excel表格，表头必须含有“企业账户号”、“企业账户名”、“实发金额”；\n" +
//				"2.带 “*” 为必填字段，填写后才能上传成功；\n" +
//				"3.若需上传其他表头，可自行在“实发金额”后添加表头，表头最多可添加20个，表头名称请控制在8个字以内；\n"
//				+
//				"4.填写的表头内容不可超过30个字；\n" +
//				"5.实发金额支持填写到2位小数；\n" +
//				"6.每次导入数据不超过5000条。\n" +
//				"\n" +
//				"注：请勿删除填写须知，删除后将导致文件上传失败\n" +
//				"\n" +
//				"表头示例：";
//		headContent.add(0, remake);
//		return headContent;
//	}
//
//	List<FileContent> fileContentList = dbFileContentService.list(
//		Wrappers.<FileContent>lambdaQuery()
//			.eq(FileContent::getBatchId, fileBatch.getId())
//			.orderByAsc(FileContent::getRowNum)
//	);
//	List<List<Object>> contentList = fileContentList.stream().map(fileContent -> {
//		List<Object> rowList = new ArrayList<>();
//		String fieldsValue = fileContent.getFieldsValue();
//		JSONObject contentObj = JSONObject.parseObject(fieldsValue);
//		for (int columnIndex = 0, length = headList.size(); columnIndex < length; columnIndex++) {
//			Object content = contentObj.get(columnIndex);
//			rowList.add(content == null ? "" : content);
//		}
//		rowList.add(fileContent.getCheckMessage());
//		return rowList;
//	}).collect(Collectors.toList());
//
//}
//
