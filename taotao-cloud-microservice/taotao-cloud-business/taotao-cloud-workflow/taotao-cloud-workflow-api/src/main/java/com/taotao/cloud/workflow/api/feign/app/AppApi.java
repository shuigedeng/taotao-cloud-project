package com.taotao.cloud.workflow.api.feign.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//@FeignClient(name = FeignName.APP_SERVER_NAME, fallback = AppApiFallback.class, path = "/Data")
public interface AppApi {

    /**
     * 删除app常用数据
     *
     * @return
     */
    @GetMapping("/deleObject/{id}")
    void deleObject(@PathVariable("id") String id);

}
