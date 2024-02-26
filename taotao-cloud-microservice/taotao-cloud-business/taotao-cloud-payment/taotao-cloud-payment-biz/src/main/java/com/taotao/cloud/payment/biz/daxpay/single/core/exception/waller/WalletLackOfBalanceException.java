package cn.bootx.platform.daxpay.exception.waller;

import cn.bootx.platform.common.core.exception.BizException;
import com.taotao.cloud.payment.biz.daxpay.single.core.code.DaxPayErrorCode;

/**
 * 余额不足异常
 *
 * @author xxm
 * @since 2020/12/8
 */
public class WalletLackOfBalanceException extends BizException {

    public WalletLackOfBalanceException() {
        super(DaxPayErrorCode.WALLET_BALANCE_NOT_ENOUGH, "余额不足异常");
    }

}
