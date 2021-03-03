/**
 * Project Name: projects
 * Package Name: com.taotao.cloud.reactive.auth
 * Descroption:
 * Date: 2020/9/10 17:00
 * Author: dengtao
 */
package com.taotao.cloud.reactive.auth.config;

/**
 * 〈〉<br>
 *
 * @author dengtao
 * @since v1.0.0
 * @version v1.0.0
 * @see
 * @create 2020/9/10 17:00
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");
    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
