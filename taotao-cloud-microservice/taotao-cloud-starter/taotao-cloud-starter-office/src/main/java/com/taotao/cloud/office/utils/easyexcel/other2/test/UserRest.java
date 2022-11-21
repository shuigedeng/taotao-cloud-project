//package com.taotao.cloud.office.utils.easyexcel.other2.test;
//
//import com.alibaba.excel.EasyExcelFactory;
//import com.alibaba.fastjson.JSON;
//import com.taotao.cloud.office.utils.easyexcel.other2.EasyExcelListener;
//import com.taotao.cloud.office.utils.easyexcel.other2.ExcelCheckErrDto;
//import com.zhy.easyexceldemo.common.BaseRest;
//import com.zhy.easyexceldemo.common.Result;
//import com.zhy.easyexceldemo.easyexcel.EasyExcelUtils;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
///**
// * @author zhyest
// * @title: UserRest
// * @projectName easyexceldemo
// * @description: 用户rest
// * @date 2020/1/1611:30
// */
//@RestController
//@RequestMapping("/user")
//public class UserRest extends BaseRest {
//
//
//	@Autowired
//	private UserService userService;
//
//	/**
//	 * @param response
//	 * @return void
//	 * @throws
//	 * @description: 导出测试
//	 * @author zhy
//	 * @date 2020/1/16 11:58
//	 */
//	@GetMapping("/exportExcel")
//	public void exportExcel(HttpServletResponse response) throws IOException {
//		List<User> userList = new ArrayList<>();
//		User user1 = new User();
//		user1.setName("张三");
//		user1.setAge(10);
//		user1.setBirthday(new Date());
//		user1.setSex("男");
//		userList.add(user1);
//		List<UserExcelDto> userExcelDtos = JSON.parseArray(JSON.toJSONString(userList),
//			UserExcelDto.class);
//		EasyExcelUtils.webWriteExcel(response, userExcelDtos, UserExcelDto.class, "用户基本信息");
//	}
//
//	/**
//	 * @param response
//	 * @param file
//	 * @return com.zhy.easyexceldemo.common.Result
//	 * @throws
//	 * @description: 导入测试
//	 * @author zhy
//	 * @date 2020/1/16 11:59
//	 */
//	@PostMapping("/importExcel")
//	public Result importExcel(HttpServletResponse response, @RequestParam MultipartFile file)
//		throws IOException {
//		EasyExcelListener easyExcelListener = new EasyExcelListener(userService,
//			UserExcelDto.class);
//		EasyExcelFactory.read(file.getInputStream(), UserExcelDto.class, easyExcelListener).sheet()
//			.doRead();
//		List<ExcelCheckErrDto<UserExcelDto>> errList = easyExcelListener.getErrList();
//		if (!errList.isEmpty()) {//如果包含错误信息就导出错误信息
//			List<UserExcelErrDto> excelErrDtos = errList.stream().map(excelCheckErrDto -> {
//				UserExcelErrDto userExcelErrDto = JSON.parseObject(
//					JSON.toJSONString(excelCheckErrDto.getT()), UserExcelErrDto.class);
//				userExcelErrDto.setErrMsg(excelCheckErrDto.getErrMsg());
//				return userExcelErrDto;
//			}).collect(Collectors.toList());
//			EasyExcelUtils.webWriteExcel(response, excelErrDtos, UserExcelErrDto.class,
//				"用户导入错误信息");
//		}
//		return addSucResult();
//	}
//}
