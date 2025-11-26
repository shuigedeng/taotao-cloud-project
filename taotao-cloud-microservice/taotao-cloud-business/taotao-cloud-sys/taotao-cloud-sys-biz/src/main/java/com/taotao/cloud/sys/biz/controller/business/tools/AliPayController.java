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

package com.taotao.cloud.sys.biz.controller.business.tools;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.constant.CommonConstants;
import com.taotao.boot.common.enums.AliPayStatusEnum;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.annotation.NotAuth;
import com.taotao.cloud.sys.biz.model.vo.alipay.TradeVO;
import com.taotao.cloud.sys.biz.model.entity.config.AlipayConfig;
import com.taotao.cloud.sys.biz.service.business.IAlipayConfigService;
import com.taotao.cloud.sys.biz.utils.AlipayUtils;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AliPayController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 16:19:02
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-支付宝管理API", description = "工具管理端-支付宝管理API")
@RequestMapping("/sys/tools/aliPay")
public class AliPayController {

    private final AlipayUtils alipayUtils;
    private final IAlipayConfigService alipayService;

    @Operation(summary = "查询阿里支付配置信息", description = "查询阿里支付配置信息", method = CommonConstants.GET)
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @GetMapping
    public Result<AlipayConfig> get() {
        return Result.success(alipayService.find());
    }

    @Operation(summary = "配置支付宝", description = "配置支付宝")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PutMapping
    public Result<Boolean> payConfig(@Validated @RequestBody AlipayConfig alipayConfig) {
        alipayConfig.setId(1L);
        alipayService.update(alipayConfig);
        return Result.success(true);
    }

    @Operation(summary = "支付宝PC网页支付", description = "支付宝PC网页支付")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PostMapping(value = "/toPayAsPC")
    public Result<String> toPayAsPc(@Validated @RequestBody TradeVO trade) throws Exception {
        AlipayConfig aliPay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsPc(aliPay, trade);
        return Result.success(payUrl);
    }

    @Hidden
    @NotAuth
    @Operation(summary = "支付之后跳转的链接", description = "支付之后跳转的链接")
    @RequestLogger
    @GetMapping("/return")
    public Result<Boolean> returnPage(HttpServletRequest request, HttpServletResponse response) {
        AlipayConfig alipay = alipayService.find();
        response.setContentType("text/html;charset=" + alipay.getCharset());
        // 内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipay)) {
            // 商户订单号
            String outTradeNo = new String(
                    request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付宝交易号
            String tradeNo = new String(
                    request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            LogUtils.info("商户订单号" + outTradeNo + "  " + "第三方交易号" + tradeNo);

            // 根据业务需要返回数据，这里统一返回OK
            return Result.success(true);
            // return new ResponseEntity<>("payment successful", HttpStatus.OK);
        } else {
            // 根据业务需要返回数据
            return Result.success(false);
            // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "支付宝手机网页支付", description = "支付宝手机网页支付")
    @RequestLogger
    @PreAuthorize("@el.check('admin','timing:list')")
    @PostMapping(value = "/toPayAsWeb")
    public Result<String> toPayAsWeb(@Validated @RequestBody TradeVO trade) throws Exception {
        AlipayConfig alipay = alipayService.find();
        trade.setOutTradeNo(alipayUtils.getOrderCode());
        String payUrl = alipayService.toPayAsWeb(alipay, trade);
        return Result.success(payUrl);
    }

    @Operation(
            summary = "支付异步通知(要公网访问)，接收异步通知，检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，根据trade_status进行后续业务处理",
            description = "支付异步通知(要公网访问)，接收异步通知，检查通知内容app_id、out_trade_no、total_amount是否与请求中的一致，根据trade_status进行后续业务处理")
    @RequestLogger
    @Hidden
    @RequestMapping("/notify")
    @NotAuth
    public Result<Boolean> notify(HttpServletRequest request) {
        AlipayConfig alipay = alipayService.find();
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 内容验签，防止黑客篡改参数
        if (alipayUtils.rsaCheck(request, alipay)) {
            // 交易状态
            String tradeStatus = new String(
                    request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 商户订单号
            String outTradeNo = new String(
                    request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付宝交易号
            String tradeNo = new String(
                    request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 付款金额
            String totalAmount = new String(
                    request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 验证
            if (tradeStatus.equals(AliPayStatusEnum.SUCCESS.getValue())
                    || tradeStatus.equals(AliPayStatusEnum.FINISHED.getValue())) {
                // 验证通过后应该根据业务需要处理订单
            }
            return Result.success(true);
            // return new ResponseEntity<>(HttpStatus.OK);
        }
        return Result.success(false);
        // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
