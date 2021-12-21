package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 预存款日志实体
 *
 * @since 2020-02-25 14:10:16
 */
@Entity
@Table(name = WalletLog.TABLE_NAME)
@TableName(WalletLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = WalletLog.TABLE_NAME, comment = "钱包变动日志")
public class WalletLog extends BaseSuperEntity<WalletLog, Long> {

	public static final String TABLE_NAME = "li_wallet_log";

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

	@Column(name = "money", nullable = false, columnDefinition = "decimal(10,2) not null comment '金额'")
	private BigDecimal money;

	/**
	 * @see DepositServiceTypeEnum
	 */
	@Column(name = "service_type", nullable = false, columnDefinition = "varchar(32) not null comment '业务类型'")
	private String serviceType;

	@Column(name = "detail", nullable = false, columnDefinition = "varchar(32) not null comment '日志明细'")
	private String detail;

	///**
	// * 构建新的预存款日志对象
	// *
	// * @param memberName            会员名称
	// * @param memberWalletUpdateDTO 变动模型
	// */
	//public WalletLog(String memberName, MemberWalletUpdateDTO memberWalletUpdateDTO) {
	//	this.setMemberId(memberWalletUpdateDTO.getMemberId());
	//	this.setMemberName(memberName);
	//	this.setMoney(memberWalletUpdateDTO.getMoney());
	//	this.setDetail(memberWalletUpdateDTO.getDetail());
	//	this.setServiceType(memberWalletUpdateDTO.getServiceType());
	//}
	//
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
