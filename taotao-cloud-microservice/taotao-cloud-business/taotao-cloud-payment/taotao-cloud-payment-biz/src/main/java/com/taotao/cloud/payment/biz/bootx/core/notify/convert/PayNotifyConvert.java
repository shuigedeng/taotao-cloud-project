package com.taotao.cloud.payment.biz.bootx.core.notify.convert;

import com.taotao.cloud.payment.biz.bootx.core.notify.entity.PayNotifyRecord;
import com.taotao.cloud.payment.biz.bootx.dto.notify.PayNotifyRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**   
* 转换
* @author xxm  
* @date 2021/6/22 
*/
@Mapper
public interface PayNotifyConvert {
    PayNotifyConvert CONVERT = Mappers.getMapper(PayNotifyConvert.class);

    PayNotifyRecordDto convert(PayNotifyRecord in);
}
