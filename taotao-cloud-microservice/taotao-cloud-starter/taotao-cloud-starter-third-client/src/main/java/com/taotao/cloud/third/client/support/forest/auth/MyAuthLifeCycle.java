package com.taotao.cloud.third.client.support.forest.auth;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.lifecycles.MethodAnnotationLifeCycle;
import com.dtflys.forest.reflection.ForestMethod;
import com.taotao.cloud.common.utils.secure.Base64Utils;

/**
 *  MyAuthLifeCycle 为自定义的 @MyAuth 注解的生命周期类
 * 因为 @MyAuth 是针对每个请求方法的，所以它实现自 MethodAnnotationLifeCycle 接口
 * MethodAnnotationLifeCycle 接口带有泛型参数
 * 第一个泛型参数是该生命周期类绑定的注解类型
 * 第二个泛型参数为请求方法返回的数据类型，为了尽可能适应多的不同方法的返回类型，这里使用 Object
 */
public class MyAuthLifeCycle implements MethodAnnotationLifeCycle<MyAuth, Object> {


    /**
     * 当方法调用时调用此方法，此时还没有执行请求发送
     * 次方法可以获得请求对应的方法调用信息，以及动态传入的方法调用参数列表
     */
    @Override
    public void onInvokeMethod(ForestRequest request, ForestMethod method, Object[] args) {
        System.out.println("Invoke Method '" + method.getMethodName() + "' Arguments: " + args);
    }

    /**
     * 发送请求前执行此方法，同拦截器中的一样
     */
    @Override
    public boolean beforeExecute(ForestRequest request) {
        // 通过getAttribute方法获取自定义注解中的属性值
        // getAttribute第一个参数为request对象，第二个参数为自定义注解中的属性名
        String username = (String) getAttribute(request, "username");
        String password = (String) getAttribute(request, "password");
        // 使用Base64进行加密
        String basic = "MyAuth " + Base64Utils.encode("{" + username + ":" + password + "}");
        // 调用addHeader方法将加密结构加到请求头MyAuthorization中
        request.addHeader("MyAuthorization", basic);
        return true;
    }

    /**
     * 此方法在请求方法初始化的时候被调用
     */
	@Override
	public void onMethodInitialized(ForestMethod method,
		MyAuth annotation) {

        System.out.println("Method '" + method.getMethodName() + "' Initialized, Arguments: " + annotation);
	}
}
