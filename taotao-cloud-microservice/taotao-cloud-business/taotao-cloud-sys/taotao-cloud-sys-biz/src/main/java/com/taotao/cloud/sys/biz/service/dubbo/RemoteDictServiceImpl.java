package com.taotao.cloud.sys.biz.service.dubbo;

import com.taotao.cloud.sys.api.dubbo.RemoteDictService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 字典服务
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteDictServiceImpl implements RemoteDictService {

	//private final ISysDictTypeService sysDictTypeService;
	//
	//
	//@Override
	//public List<SysDictData> selectDictDataByType(String dictType) {
	//	return sysDictTypeService.selectDictDataByType(dictType);
	//}

}
