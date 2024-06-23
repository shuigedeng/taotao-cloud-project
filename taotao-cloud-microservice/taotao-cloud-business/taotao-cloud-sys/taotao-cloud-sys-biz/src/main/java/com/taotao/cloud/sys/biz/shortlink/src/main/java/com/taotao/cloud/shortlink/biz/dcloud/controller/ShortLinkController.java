package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.controller;


import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.controller.request.ShortLinkDelRequest;
import net.xdclass.controller.request.ShortLinkPageRequest;
import net.xdclass.controller.request.ShortLinkUpdateRequest;
import net.xdclass.service.ShortLinkService;
import net.xdclass.util.JsonData;
import net.xdclass.vo.ShortLinkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 刘森飚
 * @since 2023-01-18
 */
@RestController
@RequestMapping("/api/link/v1")
public class ShortLinkController {

    @Autowired
    private ShortLinkService shortLinkService;


    @Value("${rpc.token}")
    private String rpcToken;

    @GetMapping("check")
    public JsonData check(@RequestParam("shortLinkCode") String shortLinkCode, HttpServletRequest request){
        String token = request.getHeader("rpc-token");
        if(rpcToken.equalsIgnoreCase(token)){
            ShortLinkVO shortLinkVO = shortLinkService.parseShortLinkCode(shortLinkCode);
            return shortLinkVO == null ? JsonData.buildError("短链不存在"):JsonData.buildSuccess();
        }else {
            return JsonData.buildError("非法访问");
        }
    }



    /**
     * 新增短链
     *
     * @param request
     * @return
     */
    @PostMapping("add")
    public JsonData createShortLink(@RequestBody ShortLinkAddRequest request) {
        JsonData jsonData = shortLinkService.createShortLink(request);
        return jsonData;
    }


    /**
     * 分页查找短链
     */

    @RequestMapping("page")
    public JsonData pageByGroupId(@RequestBody ShortLinkPageRequest request) {
        Map<String, Object> result = shortLinkService.pageByGroupId(request);
        return JsonData.buildSuccess(result);
    }


    /**
     * 删除短链
     * @param request
     * @return
     */
    @PostMapping("del")
    public JsonData del(@RequestBody ShortLinkDelRequest request){
        JsonData jsonData = shortLinkService.del(request);
        return jsonData;
    }



    /**
     * 更新短链
     * @param request
     * @return
     */
    @PostMapping("update")
    public JsonData update(@RequestBody ShortLinkUpdateRequest request){
        JsonData jsonData = shortLinkService.update(request);
        return jsonData;
    }
}

