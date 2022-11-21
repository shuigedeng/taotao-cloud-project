//package com.taotao.cloud.office.utils.easyexcel.other;
//
//import com.xxx.xxx.util.CommonResponse;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
///**
// * 事件控制器
// */
//@Slf4j
//@RestController
//@RequestMapping("/api/excel")
//public class ExcelController {
//
//	/**
//	 * excel模板下载
//	 */
//	@RequestMapping(value = "/template", method = RequestMethod.GET)
//	public CommonResponse<String> template(HttpServletResponse response) {
//		String fileName = "导入模板下载" + System.currentTimeMillis();
//		try {
//			ExcelUtil.export(fileName, null, ImportExcelVo.class, response);
//		} catch (Exception e) {
//			return CommonResponse.error("模板下载失败" + e.getMessage());
//		}
//		return CommonResponse.success("模板下载成功！");
//	}
//
//	/**
//	 * Excel批量导入数据
//	 *
//	 * @param file 导入文件
//	 */
//	@RequestMapping(value = "/import", method = RequestMethod.POST)
//	public CommonResponse<String> importEvents(MultipartFile file) {
//		try {
//			List<?> list = ExcelUtil.importExcel(file, ImportExcelVo.class);
//			System.out.println(list);
//			return CommonResponse.success("数据导入完成");
//		} catch (Exception e) {
//			return CommonResponse.error("数据导入失败！" + e.getMessage());
//		}
//	}
//
//
//	/**
//	 * excel数据导出
//	 *
//	 * @param size 导出条数， 也可以是用户需要导出数据的条件
//	 * @return
//	 */
//	@RequestMapping(value = "/export", method = RequestMethod.GET)
//	public CommonResponse<String> export(Long size, HttpServletResponse response) {
//		// 模拟根据条件在数据库查询数据
//		ArrayList<ExportExcelVo> excelVos = new ArrayList<>();
//		for (int i = 1; i <= size; i++) {
//			ExportExcelVo excelVo = new ExportExcelVo();
//			excelVo.setContact(String.valueOf(10000000000L + i));
//			excelVo.setName("公司名称" + i);
//			excelVo.setCreditCode("社会性用代码" + i);
//			excelVo.setProvince("地区" + i);
//			excelVo.setLegalPerson("法人" + i);
//			excelVo.setFormerName("曾用名" + i);
//			excelVo.setStockholder("投资人" + i);
//			excelVo.setCreateTime(
//				new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()));
//			excelVos.add(excelVo);
//		}
//
//		String fileName = "数据导出" + System.currentTimeMillis();
//
//		try {
//			ExcelUtil.export(fileName, excelVos, ExportExcelVo.class, response);
//		} catch (Exception e) {
//			return CommonResponse.error("数据导出成功" + e.getMessage());
//		}
//		return CommonResponse.success("数据导出失败！");
//	}
//
//
//}
