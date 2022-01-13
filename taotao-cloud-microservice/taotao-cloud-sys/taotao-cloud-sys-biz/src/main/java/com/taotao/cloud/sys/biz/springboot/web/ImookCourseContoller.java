package com.taotao.cloud.sys.biz.springboot.web;

import com.hrhx.springboot.crawler.ImookCourseCrawler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author duhongming
 * @version 1.0
 * @description TODO
 * @date 2019-11-30 08:24
 */
@RestController
public class ImookCourseContoller {

    @RequestMapping("/imook/execute")
    public void execute() throws Exception {
        new ImookCourseCrawler(ImookCourseCrawler.class.getName(),false);
    }

}
