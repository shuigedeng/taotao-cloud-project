package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.controller;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.ShortLinkStateEnum;
import net.xdclass.service.LogService;
import net.xdclass.service.ShortLinkService;
import net.xdclass.util.CommonUtil;
import net.xdclass.vo.ShortLinkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘森飚
 * @since 2023-01-18
 */

@Controller
@Slf4j
public class LinkApiController {

    @Autowired
    private ShortLinkService shortLinkService;

    @Autowired
    private LogService logService;


    /**
     * 低于1980的则是永久有效，毫秒
     * 1980-01-01 00:00:00  -> 315504000000L
     */
    private static long FOREVER_TIME = 315504000000L;



    /**
     * 解析 301还是302，这边是返回http code是302
     * <p>
     * 知识点一，为什么要用 301 跳转而不是 302 呐？
     * <p>
     * 301 是永久重定向，302 是临时重定向。
     * <p>
     * 短地址一经生成就不会变化，所以用 301 是同时对服务器压力也会有一定减少
     * <p>
     * 但是如果使用了 301，无法统计到短地址被点击的次数。
     * <p>
     * 所以选择302虽然会增加服务器压力，但是有很多数据可以获取进行分析
     * @return
     */
    @GetMapping(path = "/{shortLinkCode}")
    public void dispatch(@PathVariable("shortLinkCode")String shortLinkCode,
                         HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("短链码:{}", shortLinkCode);
            //判断短链码是否违规
            if (isLetterDigit(shortLinkCode)) {
                //查找短链
                ShortLinkVO shortLinkVO = shortLinkService.parseShortLinkCode(shortLinkCode);
                if(shortLinkVO != null){
                    logService.recordShortLinkLog(request,shortLinkCode,shortLinkVO.getAccountNo());
                }
                //判断是否过期和可用
                if (isVisitable(shortLinkVO)) {
                    String originalUrl = CommonUtil.removeUrlPrefix(shortLinkVO.getOriginalUrl());
                    response.setHeader("Location",originalUrl);
                    //302跳转
                    response.setStatus(HttpStatus.FOUND.value());
                } else {
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                    return;
                }
            }
        }catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }




    /**
     * 判断短链是否可用
     *
     * @param shortLinkVO
     * @return
     */
    private static boolean isVisitable(ShortLinkVO shortLinkVO) {
        if ((shortLinkVO != null && shortLinkVO.getExpired().getTime() > CommonUtil.getCurrentTimestamp())) {
            if (ShortLinkStateEnum.ACTIVE.name().equalsIgnoreCase(shortLinkVO.getState())) {
                return true;
            }
        } else if ((shortLinkVO != null && shortLinkVO.getExpired().getTime() < FOREVER_TIME)) {
            if (ShortLinkStateEnum.ACTIVE.name().equalsIgnoreCase(shortLinkVO.getState())) {
                return true;
            }
        }
        return false;
    }




    /**
     * 仅包括数字和字母
     * @param str
     * @return
     */
    private static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }

}
