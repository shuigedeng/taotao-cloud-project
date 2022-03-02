package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.sys.api.dto.mongo.CollectionDto;
import com.taotao.cloud.sys.api.dto.mongo.MongoQueryParam;
import com.taotao.cloud.sys.biz.service.MongoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * MongoController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:53:37
 */
@Validated
@RestController
@Tag(name = "工具管理-mongo管理API", description = "工具管理-mongo管理API")
@RequestMapping("/sys/tools/mongo")
public class MongoController {

    @Autowired
    private MongoService mongoService;

    /**
     * 查询所有的库
     */
    @GetMapping("/databaseNames")
    public List<String> databaseNames(@NotNull String connName) throws IOException {
        return mongoService.databaseNames(connName);
    }

    /**
     * 查询某个库里的所有集合
     */
    @GetMapping("/collectionNames/{databaseName}")
    public List<CollectionDto> collectionNames(@NotNull String connName, @PathVariable("databaseName") String databaseName) throws IOException {
        return mongoService.collectionNames(connName,databaseName);
    }

    /**
     * mongo 分页数据查询
     * @param mongoQueryParam mongo 查询参数
     * @param pageParam 分页参数
     */
    @GetMapping("/queryPage")
    public PageResponseDto<List<String>> queryPage(@Valid MongoQueryParam mongoQueryParam,
	    PageParam pageParam) throws IOException, ClassNotFoundException {
        return mongoService.queryDataPage(mongoQueryParam,pageParam);
    }
}
