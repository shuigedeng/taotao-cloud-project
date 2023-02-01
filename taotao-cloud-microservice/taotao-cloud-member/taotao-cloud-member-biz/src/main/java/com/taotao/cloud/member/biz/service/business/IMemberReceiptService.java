package com.taotao.cloud.member.biz.service.business;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.member.api.model.page.MemberReceiptPageQuery;
import com.taotao.cloud.member.api.model.vo.MemberReceiptAddVO;
import com.taotao.cloud.member.biz.model.entity.MemberReceipt;


/**
 * 会员发票业务层
 */
public interface IMemberReceiptService extends IService<MemberReceipt> {

	/**
	 * 查询会员发票列表
	 *
	 * @param memberReceiptPageQuery 会员发票信息
	 * @return 会员发票分页
	 */
	IPage<MemberReceipt> getPage(MemberReceiptPageQuery memberReceiptPageQuery);

	/**
	 * 添加会员发票信息
	 *
	 * @param memberReceiptAddVO 会员发票信息
	 * @param memberId           会员ID
	 * @return 操作状态
	 */
	Boolean addMemberReceipt(MemberReceiptAddVO memberReceiptAddVO, Long memberId);

	/**
	 * 修改会员发票信息
	 *
	 * @param memberReceiptAddVO 会员发票信息
	 * @param memberId           会员ID
	 * @return 操作状态
	 */
	Boolean editMemberReceipt(MemberReceiptAddVO memberReceiptAddVO, Long memberId);

	/**
	 * 删除会员发票信息
	 *
	 * @param id 发票ID
	 * @return 操作状态
	 */
	Boolean deleteMemberReceipt(Long id);

}
