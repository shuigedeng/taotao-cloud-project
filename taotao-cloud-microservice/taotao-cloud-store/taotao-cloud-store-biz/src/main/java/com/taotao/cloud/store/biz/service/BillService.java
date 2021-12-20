package com.taotao.cloud.store.biz.service;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.store.api.dto.BillSearchParams;
import com.taotao.cloud.store.api.vo.BillListVO;
import com.taotao.cloud.store.biz.entity.Bill;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.cache.annotation.CacheConfig;

/**
 * 结算单业务层
 *
 * @author Chopper
 * @since 2020/11/17 4:28 下午
 */
@CacheConfig(cacheNames = "bill")
public interface BillService extends IService<Bill> {

	/**
	 * 生成结算单
	 *
	 * @param storeId   商家ID
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 */
	void createBill(String storeId, Date startTime, DateTime endTime);


	/**
	 * 立即结算 用于关闭商家，立即结算使用
	 *
	 * @param storeId
	 * @param endTime 结束时间
	 */
	void immediatelyBill(String storeId, Long endTime);

	/**
	 * 获取结算单分页
	 *
	 * @param billSearchParams 结算单搜索条件
	 * @return 结算单分页
	 */
	IPage<BillListVO> billPage(BillSearchParams billSearchParams);

	/**
	 * 商家核对结算单
	 *
	 * @param id 结算单ID
	 * @return 操作状态
	 */
	boolean check(String id);

	/**
	 * 平台结算
	 *
	 * @param id 结算单ID
	 * @return 操作状态
	 */
	boolean complete(String id);

	/**
	 * 下载结算单
	 *
	 * @param id 结算单ID
	 * @response response
	 */
	void download(HttpServletResponse response, String id);
}
