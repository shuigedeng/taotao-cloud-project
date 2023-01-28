package com.taotao.cloud.store.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.store.api.model.query.BillPageQuery;
import com.taotao.cloud.store.api.model.vo.BillListVO;
import com.taotao.cloud.store.biz.model.entity.Bill;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 结算单业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 14:58:55
 */
public interface IBillService extends IService<Bill> {

	/**
	 * 生成结算单
	 *
	 * @param storeId   商家ID
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @since 2022-06-01 14:58:55
	 */
	void createBill(Long storeId, LocalDateTime startTime, LocalDateTime endTime);


	/**
	 * 立即结算 用于关闭商家，立即结算使用
	 *
	 * @param storeId
	 * @param endTime 结束时间
	 * @since 2022-06-01 14:58:55
	 */
	void immediatelyBill(String storeId, Long endTime);

	/**
	 * 获取结算单分页
	 *
	 * @param billPageQuery 结算单搜索条件
	 * @return {@link IPage }<{@link BillListVO }>
	 * @since 2022-06-01 14:58:55
	 */
	IPage<BillListVO> billPage(BillPageQuery billPageQuery);

	/**
	 * 商家核对结算单
	 *
	 * @param id 结算单ID
	 * @return boolean
	 * @since 2022-06-01 14:58:55
	 */
	boolean check(String id);

	/**
	 * 平台结算
	 *
	 * @param id 结算单ID
	 * @return boolean
	 * @since 2022-06-01 14:58:55
	 */
	boolean complete(String id);

	/**
	 * 下载结算单
	 *
	 * @param response 响应
	 * @param id       结算单ID
	 * @since 2022-06-01 14:58:55
	 */
	void download(HttpServletResponse response, String id);
}
