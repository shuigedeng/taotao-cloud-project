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
package com.taotao.cloud.payment.biz.jeepay.payment.rqrs.payorder;

import com.jeequan.jeepay.core.constants.CS;
import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.UnifiedOrderRS;
import lombok.Data;
import lombok.experimental.*;
import com.taotao.boot.common.utils.lang.StringUtils;

/*
* 通用支付数据RS
* 根据set的值，响应不同的payDataType
*
* @author terrfly
* @site https://www.jeequan.com
* @since 2021/6/8 17:32
*/
@Data
public class CommonPayDataRS extends UnifiedOrderRS {

    /** 跳转地址 **/
    private String payUrl;

    /** 二维码地址 **/
    private String codeUrl;

    /** 二维码图片地址 **/
    private String codeImgUrl;

    /** 表单内容 **/
    private String formContent;

    @Override
    public String buildPayDataType(){

        if(StringUtils.isNotEmpty(payUrl)){
            return CS.PAY_DATA_TYPE.PAY_URL;
        }

        if(StringUtils.isNotEmpty(codeUrl)){
            return CS.PAY_DATA_TYPE.CODE_URL;
        }

        if(StringUtils.isNotEmpty(codeImgUrl)){
            return CS.PAY_DATA_TYPE.CODE_IMG_URL;
        }

        if(StringUtils.isNotEmpty(formContent)){
            return CS.PAY_DATA_TYPE.FORM;
        }

        return CS.PAY_DATA_TYPE.PAY_URL;
    }

    @Override
    public String buildPayData(){

        if(StringUtils.isNotEmpty(payUrl)){
            return payUrl;
        }

        if(StringUtils.isNotEmpty(codeUrl)){
            return codeUrl;
        }

        if(StringUtils.isNotEmpty(codeImgUrl)){
            return codeImgUrl;
        }

        if(StringUtils.isNotEmpty(formContent)){
            return formContent;
        }

        return "";
    }

}
