package com.taotao.cloud.sys.biz.springboot.web;

import com.hrhx.springboot.domain.Cnarea;
import com.hrhx.springboot.aop.ResultBean;
import com.hrhx.springboot.mongodb.repository.CnareaMongoRepository;
import com.hrhx.springboot.mysql.jpa.CnareaMysqlRepository;
import io.swagger.annotations.*;
import org.apache.pulsar.shade.io.swagger.annotations.Api;
import org.apache.pulsar.shade.io.swagger.annotations.ApiImplicitParam;
import org.apache.pulsar.shade.io.swagger.annotations.ApiImplicitParams;
import org.apache.pulsar.shade.io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author duhongming
 */
@Api(value = "全国省市区乡村五级联动数据", tags = "js调用")
@RestController
@RequestMapping(value = "/cnarea")
public class CnareaRestController {

    @Autowired
    private CnareaMysqlRepository cnareaMysqlRepository;

    //	@Autowired
    private CnareaMongoRepository cnareaMongoRepository;

    /**
     * 当Mysql请求超时的时候，能够以最快的速度切换到MongoDB
     */
    private static Boolean isMysqlConnection = true;

    @ApiOperation(value = "获取省级详细信息", notes = "/cnarea/list")
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultBean<List<Cnarea>> getAllFirstLevel() {
        List<Cnarea> cnareaList = cnareaMysqlRepository.findByLevel(0);
        return new ResultBean<>(cnareaList);
    }

    @ApiOperation(value = "获取level级以及关联上级的详细信息", notes = "/cnarea/query?id=1&level=1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cnarea", value = "地理详细信息", dataType = "Cnarea")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResultBean<List<Cnarea>> getByParentId(
            @ApiParam(required = true, name = "level", value = "省市区乡村分别代表1,2,3,4,5", defaultValue = "1")
            @RequestParam(value = "level") Integer level,
            @ApiParam(required = true, name = "code", value = "该level的code", defaultValue = "1")
            @RequestParam(value = "code") String code) {
        List<Cnarea> cnareaList = cnareaMysqlRepository.findByLevelAndParentCode(level, code);
        System.out.println("获取Level级别：" + level);
        System.out.println("获取上级Id：" + code);
        return new ResultBean<>(cnareaList);
    }

}
