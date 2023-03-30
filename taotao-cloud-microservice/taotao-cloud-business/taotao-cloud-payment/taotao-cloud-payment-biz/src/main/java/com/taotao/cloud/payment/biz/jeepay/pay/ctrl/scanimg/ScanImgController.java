/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.payment.biz.jeepay.pay.ctrl.scanimg;

import com.taotao.cloud.payment.biz.jeepay.core.utils.JeepayKit;
import com.taotao.cloud.payment.biz.jeepay.pay.ctrl.payorder.AbstractPayOrderController;
import com.taotao.cloud.payment.biz.jeepay.pay.util.CodeImgUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * jeepay 扫描图片生成器
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 17:28
 */
@RestController
@RequestMapping("/api/scan")
public class ScanImgController extends AbstractPayOrderController {

    /** 返回 图片地址信息 * */
    @RequestMapping("/imgs/{aesStr}.png")
    public void qrImgs(@PathVariable("aesStr") String aesStr) throws Exception {
        String str = JeepayKit.aesDecode(aesStr);
        int width = getValIntegerDefault("width", 200);
        int height = getValIntegerDefault("height", 200);
        CodeImgUtil.writeQrCode(response.getOutputStream(), str, width, height);
    }
}
