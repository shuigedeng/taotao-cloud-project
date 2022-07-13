
package com.taotao.cloud.im.biz.platform.common.shiro;

import com.platform.common.constant.HeadConstant;
import com.platform.common.shiro.vo.LoginUser;
import com.platform.common.utils.ServletUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Shiro工具类
 */
public class ShiroUtils {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static LoginUser getLoginUser() {
        return (LoginUser) getSubject().getPrincipal();
    }

    public static String getTokenId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getTokenId();
        }
        return ServletUtils.getRequest().getHeader(HeadConstant.TOKEN_KEY);
    }

    public static String getPhone() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getPhone();
        }
        return null;
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getUserId();
        }
        return null;
    }

}
