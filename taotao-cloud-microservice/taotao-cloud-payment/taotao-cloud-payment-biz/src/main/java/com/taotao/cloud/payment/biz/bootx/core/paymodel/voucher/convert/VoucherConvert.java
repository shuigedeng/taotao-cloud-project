package com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.convert;

import cn.bootx.payment.core.paymodel.voucher.entity.Voucher;
import cn.bootx.payment.core.paymodel.voucher.entity.VoucherPayment;
import cn.bootx.payment.dto.paymodel.voucher.VoucherDto;
import cn.bootx.payment.dto.paymodel.voucher.VoucherPaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**   
*
* @author xxm  
* @date 2022/3/14 
*/
@Mapper
public interface VoucherConvert {
    VoucherConvert CONVERT = Mappers.getMapper(VoucherConvert.class);

    VoucherDto convert(Voucher in);

    VoucherPaymentDto convert(VoucherPayment in);

}
