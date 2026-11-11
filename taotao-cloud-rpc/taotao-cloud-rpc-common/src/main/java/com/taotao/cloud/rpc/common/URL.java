package com.taotao.cloud.rpc.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * URL 统一资源定位符
 * 参考 Dubbo 设计，用于描述服务地址和参数
 * <p>
 * 格式：protocol://host:port/serviceName?key1=value1&key2=value2
 */
public class URL implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 服务接口名
     */
    private String serviceInterface;

    /**
     * 服务分组
     */
    private String group;

    /**
     * 服务版本
     */
    private String version;

    /**
     * 附加参数
     */
    private Map<String, String> parameters = new HashMap<>();

    private int weight;

    public URL() {
    }

    public URL(String protocol, String host, int port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public URL(String protocol, String host, int port, String serviceInterface) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.serviceInterface = serviceInterface;
    }

    /**
     * 从 URL 字符串解析
     */
    public static URL valueOf(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        URL result = new URL();
        String rest = url;

        // 解析协议
        int protocolEnd = rest.indexOf("://");
        if (protocolEnd >= 0) {
            if (protocolEnd > 0) {
                result.protocol = rest.substring(0, protocolEnd);
            }
            // 协议名可以为空（"://host:port"），但分隔符必须剥掉：判据曾是 `> 0`，
            // 于是 "://127.0.0.1:20880" 整串留下，后续切出来的 host 变成一个冒号。
            rest = rest.substring(protocolEnd + 3);
        }

        // query 先切，再切 path：反过来写的话 "host:port?k=v"（只有 query 没有 path）
        // 会把 "?k=v" 连着端口一起送进 parseInt，而 "host:port/?k=v" 会把 "?k=v" 当成接口名。
        String paramStr = "";
        int paramStart = rest.indexOf("?");
        if (paramStart >= 0) {
            paramStr = rest.substring(paramStart + 1);
            rest = rest.substring(0, paramStart);
        }

        String path = "";
        int pathStart = rest.indexOf("/");
        if (pathStart >= 0) {
            path = rest.substring(pathStart + 1);
            rest = rest.substring(0, pathStart);
        }

        parseHostPort(result, rest, url);
        if (!path.isEmpty()) {
            result.serviceInterface = path;
        }
        if (!paramStr.isEmpty()) {
            parseParams(result, paramStr);
        }

        return result;
    }

    private static void parseHostPort(URL result, String address, String url) {
        int close = address.indexOf(']');
        if (address.startsWith("[") && close >= 0) {
            // IPv6 字面量里合法地含 ':'，所以端口不能靠 indexOf(":") 找，必须以 ']' 为界
            result.host = address.substring(1, close);
            String tail = address.substring(close + 1);
            if (tail.isEmpty()) {
                return;
            }
            if (tail.charAt(0) != ':') {
                throw new IllegalArgumentException("Unexpected \"" + tail + "\" after ']' in url: " + url);
            }
            result.port = parsePort(tail.substring(1), url);
            return;
        }

        int colonIndex = address.indexOf(":");
        if (colonIndex > 0) {
            result.host = address.substring(0, colonIndex);
            result.port = parsePort(address.substring(colonIndex + 1), url);
        } else {
            result.host = address;
        }
    }

    private static int parsePort(String raw, String url) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            // 裸 parseInt 的消息只有 For input string: "abc"，而这串是配置文件里写的地址，
            // 报错必须指回是哪一条 url。
            throw new NumberFormatException("Invalid port \"" + raw + "\" in url: " + url);
        }
    }

    private static void parseParams(URL url, String paramStr) {
        String[] pairs = paramStr.split("&");
        for (String pair : pairs) {
            int eqIndex = pair.indexOf("=");
            if (eqIndex > 0) {
                String key = pair.substring(0, eqIndex);
                String value = pair.substring(eqIndex + 1);
                url.addParameter(key, value);
            }
        }
    }

    /**
     * 添加参数
     */
    public URL addParameter(String key, String value) {
        if (value != null) {
            parameters.put(key, value);
        }
        return this;
    }

    /**
     * 获取参数
     */
    public String getParameter(String key) {
        return parameters.get(key);
    }

    /**
     * 获取参数，带默认值
     */
    public String getParameter(String key, String defaultValue) {
        String value = parameters.get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取服务唯一标识：group/service:version
     */
    public String getServiceKey() {
        StringBuilder sb = new StringBuilder();
        if (group != null && !group.isEmpty()) {
            sb.append(group).append("/");
        }
        sb.append(serviceInterface);
        if (version != null && !version.isEmpty()) {
            sb.append(":").append(version);
        }
        return sb.toString();
    }

    /**
     * 获取地址：ip:port
     */
    public String getAddress() {
        if (host != null && host.indexOf(':') >= 0) {
            // IPv6 字面量必须重新包上方括号：getAddress() 在负载均衡和 RegistryDirectory 里是
            // map 的键，"::1:20880" 这种写法分不出哪一段是端口，两条地址会撞进同一个键。
            return "[" + host + "]:" + port;
        }
        return host + ":" + port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (protocol != null) {
            sb.append(protocol).append("://");
        }
        sb.append(getAddress());
        if (serviceInterface != null) {
            sb.append("/").append(serviceInterface);
        }
        if (!parameters.isEmpty()) {
            sb.append("?");
            boolean first = true;
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (!first) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
        }
        return sb.toString();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
