package com.taotao.cloud.sys.biz.tools.security.configs;

import com.taotao.cloud.sys.biz.tools.security.configs.access.RoleBasedVoter;
import com.taotao.cloud.sys.biz.tools.security.configs.jsonlogin.JsonLoginConfiguration;
import com.taotao.cloud.sys.biz.tools.security.configs.jwt.JwtTokenValidationConfigurer;
import com.taotao.cloud.sys.biz.tools.security.configs.jwt.LogoutTokenClean;
import com.taotao.cloud.sys.biz.tools.security.configs.whitespace.WhiteSpaceFilter;
import com.taotao.cloud.sys.biz.tools.security.service.FileUserDetailServiceImpl;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JsonLoginConfiguration jsonLoginConfiguration;
    @Autowired
    private JwtTokenValidationConfigurer jwtTokenValidationConfigurer;

    @Autowired
    private LogoutTokenClean logoutTokenClean;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private UrlSecurityPermsLoad urlPermsLoad;
    @Autowired
    private RoleBasedVoter roleBasedVoter;
    @Autowired
    private ResponseHandler responseHandler;

    @Bean
    public AccessDecisionManager accessDecisionManager(){
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new WebExpressionVoter(),
                roleBasedVoter,
                new AuthenticatedVoter());
        return new UnanimousBased(decisionVoters);
    }


    @Bean
    public WhiteSpaceFilter whiteSpaceFilter(){
        return new WhiteSpaceFilter(urlPermsLoad,tokenService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 路径权限配置
        http.authorizeRequests()
                .antMatchers("/version/**","/cron/nextExecutionTime","/plugin/visited").permitAll()
                .antMatchers("/ws/**").permitAll()
                .anyRequest().authenticated()
                .accessDecisionManager(accessDecisionManager());
//                .and().anonymous().disable();

        // 禁用 session
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().disable()
                .csrf().disable();

        // 跨域配置
        http.cors().configurationSource(corsConfigurationSource());
//                .and().headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
//                new Header("Access-control-Allow-Origin","*"),
//                new Header("Access-Control-Expose-Headers","Authorization"))));

        //拦截OPTIONS请求，直接返回header
        http.addFilterAfter(new OptionsRequestFilter(), CorsFilter.class);

        // json 登录配置
        http.apply(jsonLoginConfiguration);

        // jwt token 验证配置
        http.apply(jwtTokenValidationConfigurer);

        // 白名单配置
        http.addFilterBefore(whiteSpaceFilter(), AnonymousAuthenticationFilter.class);

        // 验证失败和授权失败提示消息
        http.exceptionHandling()
                //用来解决认证过的用户访问无权限资源时的异常
                .authenticationEntryPoint(authenticationEntryPoint)
                // 用来解决用户访问无权限资源时的异常
                .accessDeniedHandler(accessDeniedHandler);

        // 登出配置,不然默认会重定向
        final CustomLogoutHandler logoutHandler = new CustomLogoutHandler(responseHandler);
        http.logout()
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutHandler);

    }

    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","HEAD", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addExposedHeader("Authorization");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/",
                "index.html",
                "/**/*.css","/**/*.js",
                "/**/*.png","/**/*.jpg","/**/*.gif","/**/*.ico","/**/img/*","/**/images/*","/**/images/**/*",
                "/**/fonts/*"
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    @Bean
    public UserDetailsService userDetailsService(){return new FileUserDetailServiceImpl(userRepository);}

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService());
        daoProvider.setPasswordEncoder(passwordEncoder());
        return daoProvider;
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider(){
        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider();
        authenticationProvider.setTokenService(tokenService);
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(jwtAuthenticationProvider());
    }
}
