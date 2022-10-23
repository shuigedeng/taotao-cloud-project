package com.taotao.cloud.elasticsearch.esearchx;

import com.taotao.cloud.elasticsearch.esearchx.exception.NoExistException;
import com.taotao.cloud.elasticsearch.esearchx.exception.RequestException;
import okhttp3.*;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 内部Http请求工具（外部别用它）
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:09:58
 */
class PriHttpUtils {
    private final static Supplier<Dispatcher> httpClientDefaultDispatcher = () -> {
        Dispatcher temp = new Dispatcher();
        temp.setMaxRequests(Constants.HttpMaxRequests);
        temp.setMaxRequestsPerHost(Constants.HttpMaxRequestsPerHost);
        return temp;
    };

    private final static OkHttpClient httpClientDefault = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .dispatcher(httpClientDefaultDispatcher.get())
            .addInterceptor(PriHttpInterceptor.instance)
            .build();

    public static PriHttpUtils http(String url) {
        return new PriHttpUtils(url);
    }


    private final OkHttpClient _client;
    private RequestBody _body;
    private Request.Builder _builder;


    public PriHttpUtils(String url) {
        this(url, httpClientDefault);
    }

    public PriHttpUtils(String url, OkHttpClient client) {
        _builder = new Request.Builder().url(url);
        _client = client;
    }

    public PriHttpUtils timeout(PriHttpTimeout timeout) {
        if (timeout != null) {
            _builder.tag(PriHttpTimeout.class, timeout);
        }

        return this;
    }

    //@XNote("设置请求头")
    public PriHttpUtils header(String name, String value) {
        if (name == null || value == null) {
            return this;
        }

        _builder.header(name, value);
        return this;
    }

    //@XNote("设置BODY txt及内容类型")
    public PriHttpUtils bodyTxt(String txt, String contentType) {
        if (txt == null) {
            return this;
        }

        if (contentType == null) {
            _body = FormBody.create(null, txt);
        } else {
            _body = FormBody.create(MediaType.parse(contentType), txt);
        }

        return this;
    }


    //@XNote("执行请求，返回响应对象")
    public Response exec(String mothod) throws IOException {

        switch (mothod.toUpperCase()) {
            case "GET":
                _builder.method("GET", null);
                break;
            case "POST":
                _builder.method("POST", _body);
                break;
            case "PUT":
                _builder.method("PUT", _body);
                break;
            case "DELETE":
                _builder.method("DELETE", _body);
                break;
            case "PATCH":
                _builder.method("PATCH", _body);
                break;
            case "HEAD":
                _builder.method("HEAD", null);
                break;
            case "OPTIONS":
                _builder.method("OPTIONS", null);
                break;
            case "TRACE":
                _builder.method("TRACE", null);
                break;
            default:
                throw new IllegalArgumentException("This method is not supported");
        }


        Call call = _client.newCall(_builder.build());
        return call.execute();
    }

    //@XNote("执行请求，返回字符串")
    public String execAsBody(String mothod) throws IOException {
        Response tmp = exec(mothod);

        int code = tmp.code();
        String text = tmp.body().string();
        if (code >= 200 && code <= 300) {
            return text;
        } else if (code == 404) {
            throw new NoExistException(text);
        } else if (code >= 500) {
            throw new ServerException(text);
        } else {
            throw new RequestException(text);
        }
    }

    //@XNote("执行请求，返回状态码")
    public int execAsCode(String mothod) throws IOException {
        return exec(mothod).code();
    }


    //@XNote("发起GET请求，返回字符串（RESTAPI.select 从服务端获取一或多项资源）")
    public String get() throws IOException {
        return execAsBody("GET");
    }

    //@XNote("发起POST请求，返回字符串（RESTAPI.create 在服务端新建一项资源）")
    public String post() throws IOException {
        return execAsBody("POST");
    }


    //@XNote("发起PUT请求，返回字符串（RESTAPI.update 客户端提供改变后的完整资源）")
    public String put() throws IOException {
        return execAsBody("PUT");
    }

    //@XNote("发起PATCH请求，返回字符串（RESTAPI.update 客户端提供改变的属性）")
    public String patch() throws IOException {
        return execAsBody("PATCH");
    }

    //@XNote("发起DELETE请求，返回字符串（RESTAPI.delete 从服务端删除资源）")
    public String delete() throws IOException {
        return execAsBody("DELETE");
    }

    public int head() throws IOException {
        return execAsCode("HEAD");
    }
}
