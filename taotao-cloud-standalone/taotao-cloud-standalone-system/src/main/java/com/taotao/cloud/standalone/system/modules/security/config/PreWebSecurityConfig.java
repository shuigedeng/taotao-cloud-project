package com.taotao.cloud.standalone.system.modules.security.config;

import com.taotao.cloud.standalone.system.modules.security.UserDetailsServiceImpl;
import com.taotao.cloud.standalone.system.modules.security.code.img.ImageCodeFilter;
import com.taotao.cloud.standalone.system.modules.security.code.sms.SmsCodeAuthenticationSecurityConfig;
import com.taotao.cloud.standalone.system.modules.security.code.sms.SmsCodeFilter;
import com.taotao.cloud.standalone.system.modules.security.filter.PreJwtAuthenticationTokenFilter;
import com.taotao.cloud.standalone.system.modules.security.handle.PreAccessDeineHandler;
import com.taotao.cloud.standalone.system.modules.security.handle.PreAuthenticationEntryPointImpl;
import com.taotao.cloud.standalone.system.modules.security.handle.PreAuthenticationFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @Classname WebSecurityConfig
 * @Description Security配置类
 * @Author shuigedeng
 * @since 2019-05-07 09:10
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PreWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PreAuthenticationFailureHandler preAuthenticationFailureHandler;

    @Autowired
    private PreJwtAuthenticationTokenFilter preJwtAuthenticationTokenFilter;

    @Autowired
    private ImageCodeFilter imageCodeFilter;

    @Autowired
    private SmsCodeFilter smsCodeFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // 注入短信登录的相关配置
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置策略
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        imageCodeFilter.setAuthenticationFailureHandler(preAuthenticationFailureHandler);
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 短信登录配置
                .apply(smsCodeAuthenticationSecurityConfig).and()
                .apply(springSocialConfigurer).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 图标 要允许匿名访问
                .antMatchers("/login/**", "/mobile/login/**", "/favicon.ico", "/socialSignUp", "/bind", "/register/**").anonymous()
                .antMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
                .antMatchers("/auth/**").anonymous()
                .antMatchers("/sendCode/**").anonymous()
                .antMatchers("/tenant/list").anonymous()
                .antMatchers("/tenant/setting/**").anonymous()
                .antMatchers("/define/deploy/**").anonymous()
                .antMatchers("/file/**")
                .permitAll()
                // 访问/user 需要拥有admin权限
                //  .antMatchers("/user").hasAuthority("ROLE_ADMIN")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();


        // 添加自定义异常入口
        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(new PreAuthenticationEntryPointImpl())
                .accessDeniedHandler(new PreAccessDeineHandler());


        // 添加JWT filter 用户名登录
        httpSecurity
                // 添加图形证码校验过滤器
                 .addFilterBefore(imageCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加JWT验证过滤器
                .addFilterBefore(preJwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加短信验证码过滤器
                .addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}

