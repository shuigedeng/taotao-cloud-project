package com.taotao.cloud.sys.biz.springboot.web;

import com.alibaba.fastjson.JSON;
import com.hrhx.springboot.domain.StockInfo;
import com.hrhx.springboot.mysql.jpa.StockInfoRespository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author duhongming
 * @version 1.0
 * @description TODO
 * @date 2020/3/12 10:50
 */
@Controller
@Slf4j
@RequestMapping(value = "/stock")
public class StockInfoController {

    public static final String STOCK_API = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=${page}&num=80&sort=changepercent&asc=0&node=hs_a&symbol=&_s_r_a=page";
    public static final String COMPANY_API = "http://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpInfo/stockid/${stockid}.phtml";

    private static final Pattern PROVINCE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]+(?=省)");
    private static final Pattern CITY_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]+(?=市)");

    @Autowired
    private StockInfoRespository stockInfoRespository;

    @RequestMapping(value = "/list")
    public void list() throws IOException, InterruptedException {
        for (int i = 1; i <= 50; i++) {
            Document stockDoc = Jsoup.connect(STOCK_API.replace("${page}", String.valueOf(i))).get();
            String stockJson = stockDoc.select("body").text();
            TimeUnit.SECONDS.sleep(1);
            List<StockInfo> stockInfoList = JSON.parseArray(stockJson, StockInfo.class);
            stockInfoRespository.save(stockInfoList);
            System.out.println("爬取第" + i + "页");
        }
    }

    @RequestMapping(value = "/company")
    public void company() throws IOException, InterruptedException {
        List<StockInfo> stockInfos = stockInfoRespository.findByIpoDate(null);
        for (StockInfo stockInfo : stockInfos) {
            Document companyDoc = Jsoup.connect(COMPANY_API.replace("${stockid}", stockInfo.getCode())).get();
            TimeUnit.SECONDS.sleep(5);
            String ipoDate = companyDoc.select("#comInfo1 > tbody > tr:nth-child(3) > td:nth-child(4) > a").text();
            stockInfo.setIpoDate(ipoDate);
            String companyAddress = companyDoc.select("#comInfo1 > tbody > tr:nth-child(18) > td.ccl").text();
            stockInfo.setCompanyAddress(companyAddress);

            Matcher provinceMatcher = PROVINCE_PATTERN.matcher(companyAddress);
            Matcher cityMatcher = CITY_PATTERN.matcher(companyAddress);
            if (provinceMatcher.find()) {
                stockInfo.setCompanyProvince(provinceMatcher.group());
            } else if (cityMatcher.find()) {
                stockInfo.setCompanyProvince(cityMatcher.group());
            } else {
                stockInfo.setCompanyProvince(companyAddress.substring(0, 4));
            }
            stockInfoRespository.save(stockInfo);
        }
    }
//    SELECT company_province FROM  stock_info GROUP BY company_province;
//    UPDATE stock_info SET company_province = '内蒙古' WHERE company_province LIKE '内蒙%';
//    UPDATE stock_info SET company_province = '新疆' WHERE company_province LIKE '新%';
//    UPDATE stock_info SET company_province = '西藏' WHERE company_province LIKE '西藏%';
//    UPDATE stock_info SET company_province = '江苏' WHERE company_province LIKE '江苏%';
//    UPDATE stock_info SET company_province = '广西' WHERE company_province LIKE '广西%';
//    UPDATE stock_info SET company_province = '黑龙江' WHERE company_province LIKE '黑龙江%';
//    UPDATE stock_info SET company_province = '宁夏' WHERE company_province LIKE '宁夏%';
//    UPDATE stock_info SET company_province = '浙江' WHERE company_province LIKE '浙江%';
//    UPDATE stock_info SET company_province = '北京' WHERE company_province LIKE '中国北京%';
//    UPDATE stock_info SET company_province = '浙江' WHERE company_province LIKE '中国浙江%';
//    UPDATE stock_info SET company_province = '重庆' WHERE company_province LIKE '中国重庆%';
//    UPDATE stock_info SET company_province = '黑龙江' WHERE company_province LIKE '中国黑龙江%';
//    UPDATE stock_info SET company_province = '天津' WHERE company_province LIKE '天津%';
//    UPDATE stock_info SET company_province = '福建' WHERE company_province LIKE '福建%';
//    UPDATE stock_info SET company_province = '湖南' WHERE company_province LIKE '湖南%';
//    UPDATE stock_info SET company_province = '河南' WHERE company_province LIKE '河南%';
//    UPDATE stock_info SET company_province = '广东' WHERE company_province LIKE '广东%';
//    UPDATE stock_info SET company_province = '江西' WHERE company_province LIKE '江西%';
//    UPDATE stock_info SET company_province = '上海' WHERE company_province LIKE '上海%';
//    UPDATE stock_info SET company_province = '中国' WHERE company_province LIKE '中国%';
}
