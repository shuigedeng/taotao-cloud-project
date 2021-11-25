package com.taotao.cloud.apollo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;


@RefreshScope
@ConfigurationProperties(prefix = ApolloProperties.PREFIX)
public class ApolloProperties {
	public static final String PREFIX = "taotao.cloud.apollo";

    /**
     * Fields
     */
    public static String Project="Apollo";
    public static String SpringApplicationName = "spring.application.name";
    public static String BsfApolloEnabled="bsf.apollo.enabled";
    public static String AppId="app.id";
    public static String BsfEnv="bsf.env";
    public static String ApolloMeta="apollo.meta";
    public static String Env="env";
    public static String ApolloBootstrapEnabled = "apollo.bootstrap.enabled";
    public static String ApolloBootstrapNamespaces="apollo.bootstrap.namespaces";
    public static String ApolloBootstrapEagerLoadEnabled="apollo.bootstrap.eagerLoad.enabled";
    public static String ApolloCacheDir="apollo.cacheDir";
    public static String UserDir="user.dir";
}
