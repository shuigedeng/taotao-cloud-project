package com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.voucher.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.voucher.entity.Voucher;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.voucher.entity.VoucherConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.voucher.entity.VoucherRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.voucher.VoucherConfigDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.voucher.VoucherDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.voucher.VoucherRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.voucher.VoucherBatchImportParam;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.voucher.VoucherImportParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/14
 */
@Mapper
public interface VoucherConvert {

    VoucherConvert CONVERT = Mappers.getMapper(VoucherConvert.class);

    VoucherDto convert(Voucher in);

    VoucherRecordDto convert(VoucherRecord in);

    VoucherConfigDto convert(VoucherConfig in);

    Voucher convert(VoucherImportParam in);

    Voucher convert(VoucherBatchImportParam in);

}
