package com.taotao.cloud.payment.biz.bootx.core.notify.service;

import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.payment.biz.bootx.core.notify.dao.PayNotifyRecordManager;
import com.taotao.cloud.payment.biz.bootx.core.notify.entity.PayNotifyRecord;
import com.taotao.cloud.payment.biz.bootx.dto.notify.PayNotifyRecordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**   
* 回调记录
* @author xxm  
* @date 2021/7/5 
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class PayNotifyRecordService {
    private final PayNotifyRecordManager payNotifyRecordManager;

    /**
     * 分页查询
     */
    public PageResult<PayNotifyRecordDto> page(PageParam pageParam,PayNotifyRecordDto param){
        Page<PayNotifyRecord> page = payNotifyRecordManager.page(pageParam,param);
        return MpUtil.convert2DtoPageResult(page);
    }

    /**
     * 根据id查询
     */
    public PayNotifyRecordDto findById(Long id){
        return payNotifyRecordManager.findById(id)
                .map(PayNotifyRecord::toDto)
                .orElseThrow(DataNotExistException::new);
    }
}
