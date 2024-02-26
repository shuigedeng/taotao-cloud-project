package cn.bootx.platform.daxpay.exception.waller;

import cn.bootx.platform.common.core.exception.BizException;
import com.taotao.cloud.payment.biz.daxpay.single.core.code.DaxPayErrorCode;

/**
 * 钱包被禁用
 *
 * @author xxm
 * @since 2020/12/8
 */
public class WalletBannedException extends BizException {

    public WalletBannedException() {
        super(DaxPayErrorCode.WALLET_BANNED, "钱包被禁用");
    }

}
