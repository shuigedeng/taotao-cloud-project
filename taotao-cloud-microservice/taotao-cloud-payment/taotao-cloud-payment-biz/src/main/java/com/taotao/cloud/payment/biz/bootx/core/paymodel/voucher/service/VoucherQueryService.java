package com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.service;

import cn.hutool.db.PageResult;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.dao.VoucherManager;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.voucher.VoucherDto;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.voucher.VoucherParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**   
* 储值卡查询
* @author xxm  
* @date 2022/3/14 
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherQueryService {
    private final VoucherManager voucherManager;

    /**
     * 分页
     */
    public PageResult<VoucherDto> page(PageParam pageParam, VoucherParam param){
        return MpUtil.convert2DtoPageResult(voucherManager.page(pageParam,param));
    }

    /**
     * 根据id查询
     */
    public VoucherDto findById(Long id) {
        return voucherManager.findById(id).map(Voucher::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据卡号查询
     */
    public VoucherDto findByCardNo(String cardNo){
        return voucherManager.findByCardNo(cardNo).map(Voucher::toDto).orElseThrow(DataNotExistException::new);
    }
}
