package com.taotao.cloud.member.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuService;
import com.taotao.cloud.member.api.dto.EvaluationQueryParams;
import com.taotao.cloud.member.api.dto.MemberEvaluationDTO;
import com.taotao.cloud.member.api.enums.EvaluationGradeEnum;
import com.taotao.cloud.member.api.vo.EvaluationNumberVO;
import com.taotao.cloud.member.api.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.vo.MemberEvaluationVO;
import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.entity.MemberEvaluation;
import com.taotao.cloud.member.biz.mapper.MemberEvaluationMapper;
import com.taotao.cloud.member.biz.service.MemberEvaluationService;
import com.taotao.cloud.member.biz.service.MemberService;
import com.taotao.cloud.order.api.feign.IFeignOrderItemService;
import com.taotao.cloud.order.api.feign.IFeignOrderService;
import com.taotao.cloud.web.sensitive.word.SensitiveWordsFilter;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员商品评价业务层实现
 *
 * @since 2020-02-25 14:10:16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberEvaluationServiceImpl extends
	ServiceImpl<MemberEvaluationMapper, MemberEvaluation> implements
	MemberEvaluationService {

	/**
	 * 会员评价数据层
	 */
	@Resource
	private MemberEvaluationMapper memberEvaluationMapper;
	/**
	 * 订单
	 */
	@Autowired
	private IFeignOrderService orderService;
	/**
	 * 子订单
	 */
	@Autowired
	private IFeignOrderItemService orderItemService;
	/**
	 * 会员
	 */
	@Autowired
	private MemberService memberService;
	/**
	 * 商品
	 */
	@Autowired
	private IFeignGoodsSkuService goodsSkuService;
	/**
	 * rocketMq
	 */
	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	/**
	 * rocketMq配置
	 */
	@Autowired
	private RocketmqCustomProperties rocketmqCustomProperties;

	@Override
	public IPage<MemberEvaluation> managerQuery(EvaluationQueryParams queryParams) {
		//获取评价分页
		return this.page(queryParams.buildMpPage(), queryParams.queryWrapper());
	}

	@Override
	public IPage<MemberEvaluationListVO> queryPage(EvaluationQueryParams evaluationQueryParams) {
		return memberEvaluationMapper.getMemberEvaluationList(
			evaluationQueryParams.buildMpPage(), evaluationQueryParams.queryWrapper());
	}

	@Override
	public boolean addMemberEvaluation(MemberEvaluationDTO memberEvaluationDTO) {
		//获取子订单信息
		OrderItem orderItem = orderItemService.getBySn(memberEvaluationDTO.getOrderItemSn());
		//获取订单信息
		Order order = orderService.getBySn(orderItem.getOrderSn());
		//检测是否可以添加会员评价
		checkMemberEvaluation(orderItem, order);
		//获取用户信息
		Member member = memberService.getUserInfo();
		//获取商品信息
		GoodsSku goodsSku = goodsSkuService.getGoodsSkuByIdFromCache(
			memberEvaluationDTO.getSkuId());
		//新增用户评价
		MemberEvaluation memberEvaluation = new MemberEvaluation(memberEvaluationDTO, goodsSku,
			member, order);
		//过滤商品咨询敏感词
		memberEvaluation.setContent(SensitiveWordsFilter.filter(memberEvaluation.getContent()));
		//添加评价
		this.save(memberEvaluation);

		//修改订单货物评价状态为已评价
		orderItemService.updateCommentStatus(orderItem.getSn(), CommentStatusEnum.FINISHED);
		//发送商品评价消息
		String destination = rocketmqCustomProperties.getGoodsTopic() + ":"
			+ GoodsTagsEnum.GOODS_COMMENT_COMPLETE.name();
		rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(memberEvaluation),
			RocketmqSendCallbackBuilder.commonCallback());
		return true;
	}

	@Override
	public MemberEvaluationVO queryById(String id) {
		MemberEvaluation memberEvaluation = this.getById(id);
		return BeanUtil.copy(memberEvaluation, MemberEvaluationVO.class);
	}

	@Override
	public boolean updateStatus(String id, String status) {
		UpdateWrapper updateWrapper = Wrappers.update();
		updateWrapper.eq("id", id);
		updateWrapper.set("status", status.equals(SwitchEnum.OPEN.name()) ? SwitchEnum.OPEN.name()
			: SwitchEnum.CLOSE.name());
		return this.update(updateWrapper);
	}

	@Override
	public boolean delete(String id) {
		LambdaUpdateWrapper<MemberEvaluation> updateWrapper = Wrappers.lambdaUpdate();
		updateWrapper.set(MemberEvaluation::getDelFlag, true);
		updateWrapper.eq(MemberEvaluation::getId, id);
		return this.update(updateWrapper);
	}

	@Override
	public boolean reply(String id, String reply, String replyImage) {
		UpdateWrapper<MemberEvaluation> updateWrapper = Wrappers.update();
		updateWrapper.set("reply_status", true);
		updateWrapper.set("reply", reply);
		if (StringUtil.isNotBlank(replyImage)) {
			updateWrapper.set("have_reply_image", true);
			updateWrapper.set("reply_image", replyImage);
		}
		updateWrapper.eq("id", id);
		return this.update(updateWrapper);
	}

	@Override
	public EvaluationNumberVO getEvaluationNumber(String goodsId) {
		EvaluationNumberVO evaluationNumberVO = new EvaluationNumberVO();
		List<Map<String, Object>> list = this.baseMapper.getEvaluationNumber(goodsId);

		int good = 0;
		int moderate = 0;
		int worse = 0;
		for (Map<String, Object> map : list) {
			if (map.get("grade").equals(EvaluationGradeEnum.GOOD.name())) {
				good = Integer.parseInt(map.get("num").toString());
			} else if (map.get("grade").equals(EvaluationGradeEnum.MODERATE.name())) {
				moderate = Integer.parseInt(map.get("num").toString());
			} else if (map.get("grade").equals(EvaluationGradeEnum.WORSE.name())) {
				worse = Integer.parseInt(map.get("num").toString());
			}
		}
		evaluationNumberVO.setAll(good + moderate + worse);
		evaluationNumberVO.setGood(good);
		evaluationNumberVO.setModerate(moderate);
		evaluationNumberVO.setWorse(worse);
		evaluationNumberVO.setHaveImage(this.count(new QueryWrapper<MemberEvaluation>()
			.eq("have_image", 1)
			.eq("goods_id", goodsId)));

		return evaluationNumberVO;
	}

	/**
	 * 检测会员评价
	 *
	 * @param orderItem 子订单
	 * @param order     订单
	 */
	public void checkMemberEvaluation(OrderItem orderItem, Order order) {

		//根据子订单编号判断是否评价过
		if (orderItem.getCommentStatus().equals(CommentStatusEnum.FINISHED.name())) {
			throw new BusinessException(ResultEnum.EVALUATION_BigDecimal_ERROR);
		}

		//判断是否是当前会员的订单
		if (!order.getMemberId().equals(UserContext.getCurrentUser().getId())) {
			throw new BusinessException(ResultEnum.ORDER_NOT_USER);
		}
	}

}
