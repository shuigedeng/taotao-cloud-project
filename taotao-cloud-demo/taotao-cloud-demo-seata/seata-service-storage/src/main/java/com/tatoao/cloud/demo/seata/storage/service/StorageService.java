package com.tatoao.cloud.demo.seata.storage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tatoao.cloud.demo.seata.storage.mapper.StorageMapper;
import com.tatoao.cloud.demo.seata.storage.model.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zlt
 * @date 2019/9/14
 */
@Slf4j
@Service
public class StorageService {
	@Resource
	private StorageMapper storageMapper;

	/**
	 * 减库存
	 *
	 * @param commodityCode 商品编号
	 * @param count         数量
	 */
	//@Transactional(rollbackFor = Exception.class)
	public void deduct(String commodityCode, int count) {
		QueryWrapper<Storage> wrapper = new QueryWrapper<>();
		wrapper.setEntity(new Storage().setCommodityCode(commodityCode));
		Storage storage = storageMapper.selectOne(wrapper);
		storage.setCount(storage.getCount() - count);

		storageMapper.updateById(storage);
	}
}
