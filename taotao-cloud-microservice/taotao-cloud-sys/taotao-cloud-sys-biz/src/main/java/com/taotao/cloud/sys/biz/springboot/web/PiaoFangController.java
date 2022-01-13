package com.taotao.cloud.sys.biz.springboot.web;


import java.io.IOException;
import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/piaofang")
public class PiaoFangController {

	private static final String PIAO_FANG_URL = "https://box.maoyan.com/promovie/api/box/second.json?beginDate=2018-02-19";

	@RequestMapping("data")
	public void getPiaofangData() {
		DateTime dateTime = DateTime.parse("2011-01-01");
		int i = 0;
		while (true) {
			DateTime dateTimeTmp = dateTime.plusDays(i);
			String dateStr = dateTimeTmp.toString("yyyy-MM-dd");
//            System.out.println(dateStr);
			if (dateStr.equals("2018-02-19")) {
				break;
			}
			i++;
		}

		try {
			Connection.Response response = Jsoup.connect(PIAO_FANG_URL)
				.header("authority", "box.maoyan.com")
				.header("method", "GET")
				.header("path", "/promovie/api/box/second.json?beginDate=20180219")
				.header("scheme", "https")
				.header("accept", "*/*")
				.header("accept-encoding", "gzip, deflate, br")
				.header("accept-language", "zh-CN,zh;q=0.9")
				.header("origin", "http://piaofang.maoyan.com")
				.header("referer", "http://piaofang.maoyan.com/dashboard?date=2018-02-19")
				.header("Auser-agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36")

//                    .header("Accept", "*/*")
//                    .header("Accept-Encoding", "gzip, deflate")
//                    .header("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
//                    .header("Content-Type", "application/json;charset=utf-8")
//                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
				.ignoreContentType(true).execute();
			String trs = response.body();
			System.out.println("===" + trs);

//            for(int j=0;i<trs.size();i++){
//                Element element = trs.get(j);
//                Elements tds = element.select("td");
//                String film = tds.get(0).html();//影片
//                String integratedBoxOfficeMillion = tds.get(1).html();//综合票房(万)
//                String boxOfficeAccountedFor = tds.get(2).html();//票房占比
//                String rowFilmingTimes = tds.get(3).html();//排片场次
//                String rowFilmOfAccountedFor = tds.get(4).html();//排片占比
//                String averagePerPerson = tds.get(5).html();//场均人次
//                String AttendanceRate = tds.get(6).html();//上座率
//                System.out.print(film);
//                System.out.print(integratedBoxOfficeMillion);
//                System.out.print(boxOfficeAccountedFor);
//                System.out.print(rowFilmingTimes);
//                System.out.print(rowFilmOfAccountedFor);
//                System.out.print(averagePerPerson);
//                System.out.print(AttendanceRate);
//            }

		} catch (IOException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}


}
