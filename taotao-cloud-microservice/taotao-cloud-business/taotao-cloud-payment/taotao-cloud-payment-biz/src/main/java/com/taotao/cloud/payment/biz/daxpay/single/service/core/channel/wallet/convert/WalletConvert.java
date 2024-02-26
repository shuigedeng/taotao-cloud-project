package com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wallet.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wallet.entity.Wallet;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wallet.entity.WalletConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wallet.entity.WalletRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.entity.WeChatPayRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.wallet.WalletConfigDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.wallet.WalletDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.wallet.WalletRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.wechat.WeChatPayRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.wechat.WalletConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 转换
 *
 * @author xxm
 * @since 2021/8/20
 */
@Mapper
public interface WalletConvert {

    WalletConvert CONVERT = Mappers.getMapper(WalletConvert.class);

    WalletDto convert(Wallet in);

    WalletConfigDto convert(WalletConfig in);

    WalletConfig convert(WalletConfigParam in);

    WalletRecordDto convert(WalletRecord in);

}
