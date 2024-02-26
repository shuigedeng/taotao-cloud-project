package com.taotao.cloud.payment.biz.daxpay.single.service.core.task.notice.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.task.notice.entity.ClientNoticeRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.task.notice.entity.ClientNoticeTask;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.record.notice.ClientNoticeRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.record.notice.ClientNoticeTaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/2/23
 */
@Mapper
public interface ClientNoticeConvert {
    ClientNoticeConvert CONVERT = Mappers.getMapper(ClientNoticeConvert.class);

    ClientNoticeRecordDto convert(ClientNoticeRecord in);

    ClientNoticeTaskDto convert(ClientNoticeTask in);

}
