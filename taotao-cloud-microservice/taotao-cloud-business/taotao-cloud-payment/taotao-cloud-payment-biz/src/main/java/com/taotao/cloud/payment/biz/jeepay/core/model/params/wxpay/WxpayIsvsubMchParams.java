/*
 * Copyright (c) 2021-2031, 河北计全科技有限公司 (https://www.jeequan.com & jeequan@126.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.payment.biz.jeepay.core.model.params.wxpay;

import com.jeequan.jeepay.core.model.params.IsvsubMchParams;
import lombok.Data;
import lombok.experimental.*;

/*
 * 微信官方支付 配置参数
 *
 * @author zhuxiao
 * @site https://www.jeequan.com
 * @since 2021/6/8 18:02
 */
@Data
public class WxpayIsvsubMchParams extends IsvsubMchParams {

    /** 子商户ID **/
    private String subMchId;

    /** 子账户appID **/
    private String subMchAppId;


}
