package com.taotao.cloud.lock.kylin.key;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认 分布式锁Key生成器
 *
 * @author wangjinkui
 */
public class DefaultLockKeyBuilder implements LockKeyBuilder {

    /**
     * 方法参数名称解析
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * SpEL解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * @param method    加锁方法
     * @param args      加锁方法参数
     * @param keySuffix 联锁、红锁 key后缀
     */
    @Override
    public void buildKeySuffix(Method method, Object[] args, String[] keySuffix) {
        if (null != keySuffix && keySuffix.length > 0) {
            getSpELKeySuffix(keySuffix, method, args);
        }
    }

    /**
     * @param method         加锁方法
     * @param args           加锁方法参数
     * @param definitionKeys keys
     * @return
     */
    @Override
    public String buildKey(Method method, Object[] args, String[] definitionKeys) {
        if (null != definitionKeys && definitionKeys.length > 0) {
            return getSpELDefinitionKey(definitionKeys, method, args);
        }
        return "";
    }

    /**
     * 解析 SpEL表达式
     *
     * @param keySuffix       SpEL表达式集合
     * @param method          业务方法
     * @param parameterValues 方法参数
     */
    protected void getSpELKeySuffix(String[] keySuffix, Method method, Object[] parameterValues) {
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, NAME_DISCOVERER);
        for (int i = 0; i < keySuffix.length; i++) {
            String key = keySuffix[i];
            if (StringUtils.hasText(key)) {
                keySuffix[i] = PARSER.parseExpression(key).getValue(context, String.class);
            }
        }
    }

    /**
     * @param definitionKeys  SpEL表达式集合
     * @param method          业务方法
     * @param parameterValues 方法参数
     * @return 匹配后的锁key一部分
     */
    protected String getSpELDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, NAME_DISCOVERER);
        List<String> definitionKeyList = new ArrayList<>(definitionKeys.length);
        for (String definitionKey : definitionKeys) {
            if (StringUtils.hasText(definitionKey)) {
                String key = PARSER.parseExpression(definitionKey).getValue(context, String.class);
                definitionKeyList.add(key);
            }
        }
        return StringUtils.collectionToDelimitedString(definitionKeyList, ".", "", "");
    }

}
