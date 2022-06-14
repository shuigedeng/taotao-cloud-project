package com.taotao.cloud.payment.biz.bootx.code.paymodel;

/**   
* 微信参数
* @author xxm  
* @date 2021/6/21 
*/
public interface WeChatPayCode {

    // 认证类型
    /** 公钥 */
    int AUTH_TYPE_KEY = 1;

    /** 证书 */
    int AUTH_TYPE_CART = 2;

    /**
     * 二维码链接
     */
    String CODE_URL = "code_url";

    /**
     * 支付跳转链接
     */
    String MWEB_URL = "mweb_url";

    /**
     * 预支付交易会话ID
     */
    String PREPAY_ID = "prepayid";

    /**
     * 返回状态码
     */
    String RETURN_CODE = "return_code";

    /**
     * 返回信息
     */
    String RETURN_MSG = "return_msg";

    /**
     * 返回错误信息
     */
    String ERR_CODE_DES = "err_code_des";

    /**
     * 业务结果
     */
    String RESULT_CODE = "result_code";

    /**
     * 交易类型
     */
    String TRADE_TYPE = "trade_type";

    /**
     * 交易状态
     */
    String TRADE_STATE = "trade_state";

    /**
     * 商户订单号
     */
    String OUT_TRADE_NO = "out_trade_no";

    /**
     * 商户订单号
     */
    String ATTACH = "attach";

    // 交易状态
    /** 支付成功 */
    String TRADE_SUCCESS = "SUCCESS";
    /** 转入退款 */
    String TRADE_REFUND = "REFUND";
    /** 未支付 */
    String TRADE_NOTPAY = "NOTPAY";
    /** 已关闭 */
    String TRADE_CLOSED = "CLOSED";
    /** 已撤销(刷卡支付) */
    String TRADE_REVOKED = "REVOKED";
    /** 用户支付中 */
    String TRADE_USERPAYING = "USERPAYING";
    /** 支付失败 */
    String TRADE_PAYERROR = "PAYERROR";
    /** 已接收，等待扣款 */
    String TRADE_ACCEPT = "ACCEPT";
}
