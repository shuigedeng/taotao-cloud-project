package com.taotao.cloud.realtime.behavior.analysis.loginfail_detect;

public class LoginEvent {
    private Long userId;
    private String ip;
    private String loginState;
    private Long timestamp;

    public LoginEvent() {
    }

    public LoginEvent(Long userId, String ip, String loginState, Long timestamp) {
        this.userId = userId;
        this.ip = ip;
        this.loginState = loginState;
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLoginState() {
        return loginState;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LoginEvent{" +
                "userId=" + userId +
                ", ip='" + ip + '\'' +
                ", loginState='" + loginState + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
