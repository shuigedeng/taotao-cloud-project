package com.taotao.cloud.pay.model;

import com.egzosn.pay.common.api.PayMessageInterceptor;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.common.bean.TransferOrder;
import com.taotao.cloud.pay.bean.MerchantPayOrder;
import com.taotao.cloud.pay.bean.MerchantQueryOrder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * 商户支付服务
 *
 */
public interface PayServiceManager {

	/**
	 * 回调校验
	 *
	 * @param detailsId 商户列表id
	 * @param params    回调回来的参数集
	 * @return 签名校验 true通过
	 */

	boolean verify(String detailsId, Map<String, Object> params);

	/**
	 * 将请求参数或者请求流转化为 Map
	 *
	 * @param detailsId    商户列表id
	 * @param parameterMap 请求参数
	 * @param is           请求流
	 * @return 获得回调的请求参数
	 */
	Map<String, Object> getParameter2Map(String detailsId, Map<String, String[]> parameterMap,
		InputStream is);

	/**
	 * 跳到支付页面 针对实时支付,即时付款
	 *
	 * @param payOrder 商户支付订单信息
	 * @return 跳到支付页面
	 */
	String toPay(MerchantPayOrder payOrder);

	/**
	 * 获取支付预订单信息
	 *
	 * @param payOrder 商户支付订单信息
	 * @return 支付预订单信息
	 */
	Map<String, Object> app(MerchantPayOrder payOrder);

	/**
	 * 获取支付预订单信息
	 *
	 * @param payOrder 商户支付订单信息
	 * @return 支付预订单信息
	 */
	Map<String, Object> getOrderInfo(MerchantPayOrder payOrder);

	/**
	 * 刷卡付,pos主动扫码付款(条码付) 刷脸付
	 *
	 * @param payOrder 商户支付订单信息
	 * @return 支付结果
	 */
	Map<String, Object> microPay(MerchantPayOrder payOrder);

	/**
	 * 获取二维码图像 二维码支付
	 *
	 * @param payOrder 商户支付订单信息
	 * @return 二维码图像
	 * @throws IOException IOException
	 */
	byte[] toQrPay(MerchantPayOrder payOrder) throws IOException;

	/**
	 * 获取二维码信息 二维码支付
	 *
	 * @param payOrder 商户支付订单信息
	 * @return 二维码信息
	 */
	String getQrPay(MerchantPayOrder payOrder);

	/**
	 * 支付回调地址 方式二
	 *
	 * @param detailsId    商户列表id
	 * @param parameterMap 请求参数
	 * @param is           请求流
	 * @return 支付是否成功
	 * @throws IOException IOException 拦截器相关增加， 详情查看{@link com.egzosn.pay.common.api.PayService#addPayMessageInterceptor(PayMessageInterceptor)}
	 *                     <p>
	 *                     业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link
	 *                     com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
	 *                     </p>
	 *                     如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link
	 *                     com.egzosn.pay.common.api.DefaultPayMessageHandler}
	 */
	String payBack(String detailsId, Map<String, String[]> parameterMap, InputStream is)
		throws IOException;

	/**
	 * 查询
	 *
	 * @param order 订单的请求体
	 * @return 返回查询回来的结果集，支付方原值返回
	 */
	Map<String, Object> query(MerchantQueryOrder order);

	/**
	 * 交易关闭接口
	 *
	 * @param order 订单的请求体
	 * @return 返回支付方交易关闭后的结果
	 */
	Map<String, Object> close(MerchantQueryOrder order);


	/**
	 * 申请退款接口
	 *
	 * @param detailsId 列表id
	 * @param order     订单的请求体
	 * @return 返回支付方申请退款后的结果
	 */

	RefundResult refund(String detailsId, RefundOrder order);

	/**
	 * 查询退款
	 *
	 * @param detailsId 列表id
	 * @param order     订单的请求体
	 * @return 返回支付方查询退款后的结果
	 */
	Map<String, Object> refundQuery(String detailsId, RefundOrder order);

	/**
	 * 下载对账单
	 *
	 * @param order 订单的请求体
	 * @return 返回支付方下载对账单的结果
	 */
	Map<String, Object> downloadBill(MerchantQueryOrder order);


	/**
	 * 转账
	 *
	 * @param detailsId 列表id
	 * @param order     转账订单
	 * @return 对应的转账结果
	 */
	Map<String, Object> transfer(String detailsId, TransferOrder order);

	/**
	 * 转账查询
	 *
	 * @param detailsId 列表id
	 * @param outNo     商户转账订单号
	 * @param tradeNo   支付平台转账订单号
	 * @return 对应的转账订单
	 */
	Map<String, Object> transferQuery(String detailsId, String outNo, String tradeNo);

	/**
	 * 创建消息
	 *
	 * @param detailsId 列表id
	 * @param message   支付平台返回的消息
	 * @return 支付消息对象
	 */
	PayMessage createMessage(String detailsId, Map<String, Object> message);


	/**
	 * 获取payService具体调用类引用
	 *
	 * @param detailsId       列表id
	 * @param payServiceClass payService类
	 * @param <T>             支付服务类引用
	 * @return 具体调用类引用
	 */
	<T extends PayService> T cast(String detailsId, Class<T> payServiceClass);

}
