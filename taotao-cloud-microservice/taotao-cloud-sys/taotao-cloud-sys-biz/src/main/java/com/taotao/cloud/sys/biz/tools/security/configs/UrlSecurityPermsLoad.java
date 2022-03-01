package com.taotao.cloud.sys.biz.tools.security.configs;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 初始化时, 从每个模块去加载 url 需要的权限
 * 文件名 authority.conf
 * 文件格式 , 每行一个 url      权限限制;   url 是 ant 格式,   例:
 * /login=anon              登录路径不需要任何权限
 * /home/**=authc           首页登录就可以访问
 * /connect/**=(a||b)&&c    有 c 角色并且有 a 角色或者 b 角色时可访问连接管理
 */
@Component
public class UrlSecurityPermsLoad implements InitializingBean {

    private static final Map<String,String> urlPerms = new LinkedHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        final ClassLoader classLoader = UrlSecurityPermsLoad.class.getClassLoader();
        final Enumeration<URL> resources = classLoader.getResources("authority.conf");
        while (resources.hasMoreElements()){
            final URL url = resources.nextElement();
            UrlResource urlResource = new UrlResource(url);
            try(final InputStream inputStream = urlResource.getInputStream();){
                final List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
                for (String line : lines) {
                    if (StringUtils.isBlank(line) || line.startsWith("#")){
                        // 忽略注释和空行
                        continue;
                    }
                    // 去两端空格
                    line = StringUtils.trim(line);

                    final String[] splitLine = StringUtils.splitPreserveAllTokens(line, "=", 2);
                    if (splitLine.length != 2) {
                       log.warn("错误的权限配置:{}",line);
                       continue;
                    }
                    urlPerms.put(splitLine[0], splitLine[1]);
                }
            }
        }
    }
    AntPathMatcher antPathMatcher =  new AntPathMatcher();

    /**
     * 查找可匹配 url 的角色列表
     * @param url
     * @return
     */
    public String findMatchRoles(String url){
        final Iterator<String> iterator = urlPerms.keySet().iterator();
        while (iterator.hasNext()){
            final String next = iterator.next();
            if (antPathMatcher.match(next,url)){
                return urlPerms.get(next);
            }
        }
        return "";
    }

    /**
     * 添加 url 权限配置, 这个是加在内存中的, 不会持久化
     * 这个会加到最后
     * @param pattern
     * @param expression
     */
    public void addUrlPerm(String pattern,String expression){
        urlPerms.put(pattern,expression);
    }

    /**
     * @return 可以免登录地址列表
     */
    public List<String> findAnonUrls() {
        List<String> antMatchPatterns = new ArrayList<>();
        for (Map.Entry<String, String> urlPermEntry : urlPerms.entrySet()) {
            final String value = urlPermEntry.getValue();
            if (StringUtils.isNotBlank(value) && value.contains("anon")){
                antMatchPatterns.add(urlPermEntry.getKey());
            }
        }
        return antMatchPatterns;
    }
}
