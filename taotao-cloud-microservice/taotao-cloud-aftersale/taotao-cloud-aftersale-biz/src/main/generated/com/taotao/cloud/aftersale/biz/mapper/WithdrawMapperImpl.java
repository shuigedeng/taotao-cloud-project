package com.taotao.cloud.aftersale.biz.mapper;

import com.taotao.cloud.aftersale.api.vo.WithdrawVO;
import com.taotao.cloud.aftersale.biz.entity.Withdraw;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-01-20T16:51:15+0800",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class WithdrawMapperImpl implements WithdrawMapper {

    @Override
    public WithdrawVO withdrawToWithdrawVO(Withdraw withdraw) {
        if ( withdraw == null ) {
            return null;
        }

        WithdrawVO withdrawVO = new WithdrawVO();

        withdrawVO.setId( withdraw.getId() );
        withdrawVO.setCode( withdraw.getCode() );
        withdrawVO.setCompanyId( withdraw.getCompanyId() );
        withdrawVO.setMallId( withdraw.getMallId() );
        withdrawVO.setAmount( withdraw.getAmount() );
        withdrawVO.setBalanceAmount( withdraw.getBalanceAmount() );
        withdrawVO.setCreateTime( withdraw.getCreateTime() );
        withdrawVO.setLastModifiedTime( withdraw.getLastModifiedTime() );

        return withdrawVO;
    }
}
