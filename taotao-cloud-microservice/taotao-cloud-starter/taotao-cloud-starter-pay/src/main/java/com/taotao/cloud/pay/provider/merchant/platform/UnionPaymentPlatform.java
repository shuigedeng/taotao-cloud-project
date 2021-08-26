package com.taotao.cloud.pay.provider.merchant.platform;

import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.union.api.UnionPayConfigStorage;
import com.egzosn.pay.union.api.UnionPayService;
import com.egzosn.pay.union.bean.UnionTransactionType;
import com.taotao.cloud.pay.merchant.PaymentPlatform;
import com.taotao.cloud.pay.merchant.bean.CommonPaymentPlatformMerchantDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 银联支付平台
 *
 * @author egan
 *         <pre>
 *                 email egzosn@gmail.com
 *                 date  2019/4/4 14:35.
 *                 </pre>
 */
@Configuration(UnionPaymentPlatform.platformName)
@ConditionalOnMissingBean(UnionPaymentPlatform.class)
@ConditionalOnClass(name = {"com.egzosn.pay.union.api.UnionPayConfigStorage"})
public class UnionPaymentPlatform implements PaymentPlatform {
    protected final Log LOG = LogFactory.getLog(UnionPaymentPlatform.class);
    public static final String platformName = "unionPay";



    /**
     * 获取商户平台
     *
     * @return 商户平台
     */
    @Override
    public String getPlatform() {
        return platformName;
    }

    /**
     * 获取支付平台对应的支付服务
     *
     * @param payConfigStorage 支付配置
     * @return 支付服务
     */
    @Override
    public PayService getPayService(PayConfigStorage payConfigStorage) {
        if (payConfigStorage instanceof UnionPayConfigStorage) {
            return new UnionPayService((UnionPayConfigStorage) payConfigStorage);
        }
        UnionPayConfigStorage configStorage = new UnionPayConfigStorage();
        configStorage.setMerId(payConfigStorage.getPid());
        //是否为证书签名
        configStorage.setCertSign(true);
        configStorage.setPid(payConfigStorage.getPid());
        configStorage.setNotifyUrl(payConfigStorage.getNotifyUrl());
        configStorage.setReturnUrl(payConfigStorage.getReturnUrl());
        configStorage.setSignType(payConfigStorage.getSignType());
        configStorage.setPayType(payConfigStorage.getPayType());
        configStorage.setInputCharset(payConfigStorage.getInputCharset());
        configStorage.setTest(payConfigStorage.isTest());
        if (payConfigStorage instanceof CommonPaymentPlatformMerchantDetails) {
            CommonPaymentPlatformMerchantDetails merchantDetails = (CommonPaymentPlatformMerchantDetails) payConfigStorage;
            //设置证书对应的存储方式
            configStorage.setCertStoreType(merchantDetails.getCertStoreType());
            try {
                //中级证书路径
                configStorage.setAcpMiddleCert(merchantDetails.getKeyPublicCertInputStream());
                //根证书路径
                configStorage.setAcpRootCert(merchantDetails.getKeyCertInputStream());
                // 私钥证书路径
                configStorage.setKeyPrivateCert(merchantDetails.getKeystoreInputStream());
                //这里转变为流的方式
                configStorage.setCertStoreType(CertStoreType.INPUT_STREAM);
            } catch (IOException e) {
                LOG.error(e);
            }
            //私钥证书对应的密码
            configStorage.setKeyPrivateCertPwd(merchantDetails.getKeystorePwd());

        }else {


        }

        return new UnionPayService(configStorage);
    }

    /**
     * 获取支付平台对应的支付服务
     *
     * @param payConfigStorage  支付配置
     * @param httpConfigStorage 网络配置
     * @return 支付服务
     */
    @Override
    public PayService getPayService(PayConfigStorage payConfigStorage, HttpConfigStorage httpConfigStorage) {
        PayService payService = getPayService(payConfigStorage);
        payService.setRequestTemplateConfigStorage(httpConfigStorage);
        return payService;
    }

    @Override
    public TransactionType getTransactionType(String name) {
        return UnionTransactionType.valueOf(name);
    }


}
