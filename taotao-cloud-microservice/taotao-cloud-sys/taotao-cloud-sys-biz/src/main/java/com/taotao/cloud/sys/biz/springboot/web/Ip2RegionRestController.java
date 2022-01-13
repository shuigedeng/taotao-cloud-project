package com.taotao.cloud.sys.biz.springboot.web;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.hrhx.springboot.aop.ResultBean;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.seckill.lionsoul.ip2region.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 *
 * @author duhongming
 *
 */
@Slf4j
@Api(value="根据IP地址获取地理信息",tags="js调用")
@RestController
@RequestMapping(value = "/ipregion")
public class Ip2RegionRestController {

    /**
     * Moe IP
     * IPv4 地址归属地查询接口
     *
     * 欢迎访问 Moe IP 数据库，本站提供了接口可免费查询 IP 地址归属地。
     *
     * 数据基于纯真 IP 库 + ip2region + GeoIP2 整合而来，部分数据可能没有及时更新，如果您在查询时遇到有误的数据欢迎向我们反馈。接口不限请求 QPS 数，
     * 但是我们希望您能合理使用，毕竟维护公益服务不易。
     */
    private static final String MOE_IP_URL = "https://ip.mcr.moe/?ip={}&compress&db2";

    /**
     * 未知区域
     */
    private static final String UN_KNOW_AREA = "0";

    /**
     * 中国地区
     */
    private static final String CHINA_AREA = "中国";

	@Value("${ip.region.db.file:}")
	private String dbFile;

	@ApiOperation(value = "根据IP地址获取地理信息", notes = "/ipregion/query?ip=202.96.64.68")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public ResultBean<JSONObject> getByParentId(
			@ApiParam(required = true, name = "ip", value= "IP地址", defaultValue="202.96.64.68" )
			@RequestParam( value = "ip") String ip
			)
	throws DbMakerConfigException, NoSuchMethodException, SecurityException, IllegalAccessException,
	IllegalArgumentException, InvocationTargetException, IOException{

	    // 校验传入的ip地址是否正确
	    if (!Util.isIpAddress(ip)) {
	        throw new IllegalArgumentException("错误的IP地址");
        }

        JSONObject resultJson = new JSONObject(4);
        resultJson.put("addr", ip);
        resultJson.put("provider", "内网IP");
        resultJson.put("country", "内网IP");
        resultJson.put("area", "未知地区");

        if (StrUtil.isBlank(dbFile)) {
            String responseTxt = HttpUtil.get(StrUtil.format(MOE_IP_URL, ip));
            if (StrUtil.isNotBlank(responseTxt)) {
                JSONObject responseObj = JSONObject.parseObject(responseTxt);
                resultJson.put("country", responseObj.getString("country"));
                resultJson.put("area", responseObj.getString("area"));
                resultJson.put("provider", responseObj.getString("provider"));
            }
        } else {

		    // File file = ResourceUtils.getFile("classpath:ipdata/ip2region.db");
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, new File(dbFile).getAbsolutePath());
            Method method = searcher.getClass().getMethod("btreeSearch", String.class);
            DataBlock dataBlock = (DataBlock) method.invoke(searcher, ip);

            log.info("======> ip2region#dataBlock = {}", dataBlock);

            if (null != dataBlock) {
                // region 解析ip2region的返回结果
                String region = dataBlock.getRegion();
                String[] array = StrUtil.splitToArray(region, '|');
                if (StrUtil.isNotBlank(region) && ArrayUtil.isNotEmpty(array) &&
                        !StrUtil.equalsIgnoreCase(UN_KNOW_AREA, array[0])) {
                    resultJson.put("country", array[0]);
                    resultJson.put("provider", array[4]);
                    // ip2region国内数据才有三级行政区划
                    if (StrUtil.equalsIgnoreCase(CHINA_AREA, array[0])) {
                        resultJson.put("area", array[2] + array[3]);
                    }
                }
            }
            // endregion

            searcher.close();
        }

        return new ResultBean<JSONObject>(resultJson);
	}
}
