package com.taotao.cloud.java.javaweb.p12_myshop.entity;

public class Result {
    private String appid;
    private String bank_type;
    private String cash_fee;
    private String is_subscribe;
    private String mch_id;
    private String nonce_str;
    private String openid;
    private String out_trade_no;
    private String result_code;//支付结果
    private String return_code;
    private String sign;

    private String time_end;
    private String total_fee;//总支付价格
    private String trade_type;
    private String transaction_id;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(String cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Result(String appid, String bank_type, String cash_fee, String is_subscribe, String mch_id, String nonce_str,
                  String openid, String out_trade_no, String result_code, String return_code, String sign, String time_end,
                  String total_fee, String trade_type, String transaction_id) {
        super();
        this.appid = appid;
        this.bank_type = bank_type;
        this.cash_fee = cash_fee;
        this.is_subscribe = is_subscribe;
        this.mch_id = mch_id;
        this.nonce_str = nonce_str;
        this.openid = openid;
        this.out_trade_no = out_trade_no;
        this.result_code = result_code;
        this.return_code = return_code;
        this.sign = sign;
        this.time_end = time_end;
        this.total_fee = total_fee;
        this.trade_type = trade_type;
        this.transaction_id = transaction_id;
    }
}
