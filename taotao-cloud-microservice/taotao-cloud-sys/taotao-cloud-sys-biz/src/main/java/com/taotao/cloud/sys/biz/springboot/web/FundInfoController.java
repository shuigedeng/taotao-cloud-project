package com.taotao.cloud.sys.biz.springboot.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hrhx.springboot.domain.FundInfo;
import com.hrhx.springboot.domain.FundPublic;
import com.hrhx.springboot.mysql.jpa.FundInfoRepository;
import com.hrhx.springboot.mysql.jpa.FundPublicRepository;
import com.hrhx.springboot.mysql.service.FundPublicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author duhongming
 */
@Controller
@Slf4j
@RequestMapping(value = "/fund")
public class FundInfoController {

    @Autowired
    private FundInfoRepository fundInfoRepository;

    @Autowired
    private FundPublicRepository fundPublicRepository;

    @Resource
    private FundPublicService fundPublicService;

    private final static String FUND_PUBLIC_URL = "http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&ft=all&rs=&gs=0&sc=zzf&st=desc&sd=2010-01-01&ed=2019-11-15&qdii=&tabSubtype=,,,,,&pi=1&pn=10000&dx=1&v=0.8597183667182815";
    private final static String FUND_DATA_URL = "http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=${fundCode}&page=${page}&per=40&sdate=&edate=&rt=0.07694074122676131";

    List<String> fundCodes = Lists.newArrayList(
            //易方达蓝筹精选混合
            "005827",
            //广发中证全指建筑材料指数C
            "004857"
    );

    @RequestMapping(value = "/data/save")
    public void updateFundPublic() throws IOException {
        //获取所有基金数据
        Document doc = Jsoup.connect(FUND_PUBLIC_URL).get();
        //过滤脏数据
        String json = doc.text()
                .replace("var rankData = ", "")
                .replace(";", "");
        //解析数据
        JSONObject jsonObject = JSON.parseObject(json);
        //获取数据
        JSONArray datas = jsonObject.getJSONArray("datas");
        //遍历数据
        for (int i = 0; i < datas.size(); i++) {
            String data = datas.get(i).toString().replace("\"", "");
            String fundCode = data.split(",")[0];
            String fundName = data.split(",")[1];
            FundPublic fundPublic = new FundPublic(null, fundCode, fundName);
            fundPublicRepository.save(fundPublic);
        }

    }

    @RequestMapping(value = "/data/detail/save")
    public void updateData() throws InterruptedException {
//        List<FundPublic> fundPublicList = fundPublicRepository.findAll();
        List<FundPublic> fundPublicList = fundPublicService.getFundPublic();
        for (FundPublic fundPublic : fundPublicList) {
            if (fundCodes.contains(fundPublic.getFundCode())) {
                new FundService(fundPublic.getFundCode(), fundPublic.getFundName()).start();
                Thread.sleep(1000);
            }
        }
    }
    public class FundService extends Thread {
        private String fundCode;
        private String fundName;

        List<FundInfo> fundInfoList = new ArrayList<FundInfo>();

        /**
         * 通过构造方法给线程名字赋值
         *
         * @param fundCode
         * @param fundName
         */
        public FundService(String fundCode, String fundName) {
            // 给线程名字赋值
            super(fundCode + " " + fundName);
            this.fundCode = fundCode;
            this.fundName = fundName;
        }

        @Override
        public void run() {
            Document doc = getDoc(fundCode, 1);
            saveFundInfo(fundCode, fundName, doc, fundInfoList);

            //过滤脏数据
            String json = doc.text()
                    .replace("var apidata=", "")
                    .replace(";", "");
            //解析数据
            JSONObject jsonObject = JSON.parseObject(json);
            Integer pages = jsonObject.getInteger("pages");

            for (int i = 2; i <= pages; i++) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                doc = getDoc(fundCode, i);
                saveFundInfo(fundCode, fundName, doc, fundInfoList);
            }
            fundInfoRepository.save(fundInfoList);
            log.info("批量保存成功");
        }

    }

    private Document getDoc(String fundCode,Integer page){
        Document doc = null;
        try {
            doc = Jsoup.connect(FUND_DATA_URL
                    .replace("${fundCode}", fundCode)
                    .replace("${page}", String.valueOf(page))
            ).get();
        } catch (IOException e) {
            log.error("JSoup请求页面异常：", e);
        }
        return doc;
    }

    private void saveFundInfo(String fundCode, String fundName, Document doc, List<FundInfo> fundInfoList) {
        Elements trs = doc.select("table tbody tr");
        for (int i = 0; i < trs.size(); i++) {
            try {
                Element element = trs.get(i);
                Elements tds = element.select("td");

                String netValueDate = tds.get(0).html().substring(0, 10);
                String td1 = tds.get(1).html();
                String td2 = tds.get(2).html();
                String td3 = tds.get(3).html().replace("%", "");

                FundInfo fundInfo = new FundInfo()
                        .setFundCode(fundCode)
                        .setFundName(fundName)
                        .setNetvalueDate(netValueDate);
                if(StringUtils.isNotBlank(td1)){
                    Double unitNetValue = Double.parseDouble(td1);
                    fundInfo.setUnitNetValue(unitNetValue);
                }
                if(StringUtils.isNotBlank(td2)){
                    Double cumulativeNetValue = Double.parseDouble(td2);
                    fundInfo.setCumulativeNetValue(cumulativeNetValue);
                }
                if(StringUtils.isNotBlank(td3)){
                    Double dailyGrowthRate = Double.parseDouble(td3);
                    fundInfo.setDailyGrowthRate(dailyGrowthRate);
                }
                fundInfoList.add(fundInfo);
            } catch (Exception e) {
                log.error("JSoup解析页面异常：", e);
            }
        }

    }
}

