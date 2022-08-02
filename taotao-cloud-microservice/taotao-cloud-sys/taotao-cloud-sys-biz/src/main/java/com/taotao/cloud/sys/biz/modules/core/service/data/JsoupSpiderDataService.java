package com.taotao.cloud.sys.biz.modules.core.service.data;

import com.sanri.tools.modules.core.service.data.jsoup.Request;
import com.sanri.tools.modules.core.service.data.jsoup.Select;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.text.StringSubstitutor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static sun.net.www.protocol.http.HttpURLConnection.userAgent;

@Service
@Slf4j
public class JsoupSpiderDataService {

    /**
     * 使用类加载器上传指定类,带有 Request Select 标记 , 则可以爬取数据
     * @param className
     * @param classLoader
     * @param params
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Object spiderData(String className,ClassLoader classLoader, Map<String,String> params) throws ClassNotFoundException, IOException {
        Class<?> clazz = classLoader.loadClass(className);
        return spiderData(clazz,params);
    }

    /**
     * 爬取指定类的数据
     * @param clazz
     */
    public Object spiderData(Class<?> clazz, Map<String,String> params) throws IOException {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(params, "${", "}");
        Request request = AnnotationUtils.getAnnotation(clazz, Request.class);
        if (request == null){
            log.warn("使用此方法爬取数据,需要类上带有 Request 标记 ");
            return null;
        }
        String address = stringSubstitutor.replace(request.value());
        HttpMethod httpMethod = request.httpMethod();

        Connection connection = Jsoup.connect(address)
                .userAgent(userAgent)
                .validateTLSCertificates(false)
                .timeout(10000);

        Document document = null;
        if(httpMethod == HttpMethod.GET){
            document = connection.get();
        }else if (httpMethod == HttpMethod.POST){
            document = connection.data(params).post();
        }

        return populate(clazz,document);
    }

    /**
     * 类型注入数据
     * @param clazz
     * @param element
     * @return
     */
    private Object populate(Class clazz, Element element){
        Object object = ReflectUtils.newInstance(clazz);

        Class currentClass = clazz;
        while(currentClass != Object.class){
            try {
                Field[] declaredFields = currentClass.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    Select select = AnnotationUtils.getAnnotation(declaredField, Select.class);
                    if(select == null) {
                        continue;
                    }

                    String cssQuery = select.value();
                    if(StringUtils.isBlank(cssQuery)) {
                        continue;
                    }

                    Elements elements = element.select(cssQuery);
                    if(elements != null && elements.size() > 0){
                        Element el = elements.get(0);

                        Class<?> type = declaredField.getType();
                        if(type == List.class){
                            List list = new ArrayList();
                            ParameterizedType genericType = (ParameterizedType)declaredField.getGenericType();
                            Class listType = (Class) genericType.getActualTypeArguments()[0];
                            Select listTypeSelect = AnnotationUtils.getAnnotation(listType, Select.class);
                            Elements listEls = el.select(listTypeSelect.value());
                            Iterator<Element> iterator = listEls.iterator();
                            while (iterator.hasNext()){
                                Element currEl = iterator.next();
                                // 注入 List 对象
                                Object populate = populate(listType, currEl);
                                list.add(populate);
                            }

                            FieldUtils.writeField(declaredField,object,list,true);
                            continue;
                        }

                        String attr = select.attr();

                        String value = "";
                        if("content".equalsIgnoreCase(attr)){
                            value = el.text();
                        }else if("html".equalsIgnoreCase(attr)){
                            value = el.html();
                        }else{
                            value = el.attr(attr);
                        }

                        FieldUtils.writeField(declaredField,object,value,true);
                    }
                }
            }catch (Exception e){
                log.error("反射异常[{}]",e.getMessage(),e);
            }finally {
                currentClass = clazz.getSuperclass();
            }
        }
        return object;
    }
}
