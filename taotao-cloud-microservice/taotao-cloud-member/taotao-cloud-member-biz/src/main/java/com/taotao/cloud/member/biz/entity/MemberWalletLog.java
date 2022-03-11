package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.member.api.enums.DepositServiceTypeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 钱包变动日志表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:43:36
 */
@Entity
@Table(name = MemberWalletLog.TABLE_NAME)
@TableName(MemberWalletLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberWalletLog.TABLE_NAME, comment = "钱包变动日志表")
public class MemberWalletLog extends BaseSuperEntity<MemberWalletLog, Long> {

	public static final String TABLE_NAME = "li_wallet_log";
	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

	/**
	 * 金额
	 */
	@Column(name = "money", nullable = false, columnDefinition = "decimal(10,2) not null comment '金额'")
	private BigDecimal money;

	/**
	 * 业务类型
	 *
	 * @see DepositServiceTypeEnum
	 */
	@Column(name = "service_type", nullable = false, columnDefinition = "varchar(32) not null comment '业务类型'")
	private String serviceType;

	/**
	 * 日志明细
	 */
	@Column(name = "detail", nullable = false, columnDefinition = "varchar(32) not null comment '日志明细'")
	private String detail;

	///**
	// * 构建新的预存款日志对象
	// *
	// * @param memberName            会员名称
	// * @param memberWalletUpdateDTO 变动模型
	// */
	//public MemberWalletLog(String memberName, MemberWalletUpdateDTO memberWalletUpdateDTO) {
	//	this.setMemberId(memberWalletUpdateDTO.getMemberId());
	//	this.setMemberName(memberName);
	//	this.setMoney(memberWalletUpdateDTO.getMoney());
	//	this.setDetail(memberWalletUpdateDTO.getDetail());
	//	this.setServiceType(memberWalletUpdateDTO.getServiceType());
	//}

	///**
	// * 构建新的预存款日志对象
	// *
	// * @param memberName            会员名称
	// * @param memberWalletUpdateDTO 变动模型
	// * @param isReduce              是否是消费
	// */
	//public WalletLog(String memberName, MemberWalletUpdateDTO memberWalletUpdateDTO,
	//	boolean isReduce) {
	//	this.setMemberId(memberWalletUpdateDTO.getMemberId());
	//	this.setMemberName(memberName);
	//	this.setMoney(
	//		isReduce ? -memberWalletUpdateDTO.getMoney() : memberWalletUpdateDTO.getMoney());
	//	this.setDetail(memberWalletUpdateDTO.getDetail());
	//	this.setServiceType(memberWalletUpdateDTO.getServiceType());
	//}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
