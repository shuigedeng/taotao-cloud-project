package com.taotao.cloud.sys.biz.api.controller.tools.core.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * 创建时间:2016-9-24下午5:33:29<br>
 * 创建者:sanri<br>
 */
public class RandomUtil {
	private static Logger log = LoggerFactory.getLogger(RandomUtil.class);

	public static final String FIRST_NAME="赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄麴家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘景詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍舄璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终暨居衡步都耿满弘匡国文寇广禄阙东殴殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查後荆红游竺权逯盖益桓公晋楚闫法汝鄢涂钦仉督岳帅缑亢况后有琴商牟佘佴伯赏墨哈谯笪年爱阳佟";
	public static final String GIRL="秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";  
    public static final String BOY="伟刚勇毅俊峰强军平保东文辉力明永健鸿世广万志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘正日";

	public static JSONObject AREANO_MAP;
	/**
	 * 身份证前 6 位码
	 */
	public static List<String> AREA_CITY_MAP = new ArrayList<>();
	public static JSONObject CITY_LIST;
    public static final String[] EMAIL_SUFFIX="@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");
    public static final String[] PHONE_SEGMENT = "133,149,153,173,177,180,181,189,199,130,131,132,145,155,156,166,171,175,176,185,186,166,135,136,137,138,139,147,150,151,152,157,158,159,172,178,182,183,184,187,188,198,170".split(",");
    public static String [] ADDRESS_LIST;
    public static String [] JOBS;
    static Random random = new Random();
	static{
		InputStreamReader reader = null;
		Charset charset = StandardCharsets.UTF_8;
		final ClassLoader classLoader = RandomUtil.class.getClassLoader();
		try (
				// 必须是 getClassLoader().getResource("data/") 一个字符都不能差,不然取不到,真是坑
				// 而且只能用流来读 , 不能用 URI 的 resolve
				final InputStream addressStream = classLoader.getResourceAsStream("data/address.string");
				final InputStream cityStream = classLoader.getResourceAsStream("data/city.min.json");
				final InputStream idcodeStream = classLoader.getResourceAsStream("data/idcodearea.json");
				final InputStream jobStream = classLoader.getResourceAsStream("data/job.string");
		) {
			if (addressStream != null && cityStream != null && idcodeStream != null && jobStream != null) {
				ADDRESS_LIST = StringUtils.split(IOUtils.toString(addressStream, charset), ',');
				CITY_LIST = JSONObject.parseObject(IOUtils.toString(cityStream, charset));
				AREANO_MAP = JSONObject.parseObject(IOUtils.toString(idcodeStream, charset));
				JOBS = StringUtils.split(IOUtils.toString(jobStream, charset), ',');
			}

			// 过滤出 6 位身份证号
			final Set<String> areaNos = AREANO_MAP.keySet();
			final Iterator<String> iterator = areaNos.iterator();
			while (iterator.hasNext()){
				final String areaNo = iterator.next();
				if (StringUtils.isNotBlank(areaNo) && areaNo.length() == 6){
					AREA_CITY_MAP.add(areaNo);
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}

	/**
	 * uuid , 去掉 -
	 * @return
	 */
	public static String uuid(){
		return UUID.randomUUID().toString().replaceAll("-","");
	}

	/**
	 * 功能:生成 length 个中文 <br>
	 * 创建时间:2016-4-16上午11:24:40 
	 * 作者：sanri 
	 * */
	public static String chinese(int length, String src) {
		String ret = "";
		if(!StringUtils.isBlank(src)){
			return RandomStringUtils.random(length, src.toCharArray());
		}
		for (int i = 0; i < length; i++) {
			String str = null;
			int hightPos, lowPos; // 定义高低位
			Random random = new Random();
			hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
			lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
			byte[] b = new byte[2];
			b[0] = (new Integer(hightPos).byteValue());
			b[1] = (new Integer(lowPos).byteValue());
			try {
				str = new String(b, "GBk"); // 转成中文
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
			ret += str;
		}
		return ret;
	}
	
	/**
	 * 
	 * 功能:随机生成用户名<br>
	 * 创建时间:2017-8-13上午8:04:32<br>
	 * 作者：sanri<br>
	 * @return<br>
	 */
	public static String username(){
		boolean sex = random.nextBoolean();
		int secondNameLength = RandomUtils.nextInt(1,3);
		String firstName = RandomStringUtils.random(1, FIRST_NAME);
		String srcChars = sex ? BOY : GIRL;
		String secondName = RandomStringUtils.random(secondNameLength,srcChars );
		return firstName+secondName;
	}
	/**
	 *
	 * 功能:给定格式 ,开始时间,结束时间,生成一个在开始和结束内的日期<br>
	 * 创建时间:2016-4-16下午3:57:38<br>
	 * 作者：sanri<br>
	 * 入参说明:<br>
	 * 出参说明：字符串日期类型由 format 格式化<br>
	 * @throws ParseException<br>
	 */
	public static String date(String format,String begin,String end) throws ParseException{
		if(StringUtils.isBlank(format)){
			format = "yyyyMMdd";
		}
		long timstamp = timestamp(format, begin, end);
		return DateFormatUtils.format(timstamp, format);
	}

	public static long timstamp(){
		try {
			return timestamp(null,null,null);
		} catch (ParseException e) {
		}
		return 0 ;
	}
	/**
	 *
	 * 功能:得到由开始时间和结束时间内的一个时间戳<br>
	 * 创建时间:2016-4-16下午4:07:31<br>
	 * 作者：sanri<br>
	 * 入参说明:<br>
	 * 出参说明：如果时间给的不对,则是当前时间<br>
	 * @param format
	 * @param begin
	 * @param end
	 * @return
	 * @throws ParseException<br>
	 */
	public static long timestamp(String format,String begin,String end) throws ParseException{
		if(StringUtils.isBlank(format)){
			format = "yyyyMMdd";
		}
		Date now = new Date();
		if(StringUtils.isBlank(begin)){
			begin = DateFormatUtils.format(now,format);
		}
		if(StringUtils.isBlank(end)){
			end = DateFormatUtils.format(now,format);
		}
		String [] formats = new String []{format};
		long beginDateTime = DateUtils.parseDate(begin, formats).getTime();
		long endDateTime = DateUtils.parseDate(end, formats).getTime();
		if(beginDateTime > endDateTime){
			return now.getTime();
		}
		return RandomUtils.nextLong(beginDateTime,endDateTime);
	}

	/**
	 *
	 * 功能:生成身份证号<br>
	 * 创建时间:2016-4-16下午2:31:37<br>
	 * 作者：sanri<br>
	 * 入参说明:[area:区域号][yyyyMMdd:出生日期][sex:偶=女,奇=男]<br>
	 * 出参说明：330602 19770717 201 1<br>
	 *
	 * @param area
	 * @param yyyyMMdd
	 * @param sno
	 * @return<br>
	 */
	public static String idcard(String area, String yyyyMMdd, String sno) {
		String idCard17 = area + yyyyMMdd + sno;
		int[] validas = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char[] idCards = idCard17.toCharArray();
		int count = 0;
		for (int i = 0; i < validas.length; i++) {
			count += Integer.valueOf(String.valueOf(idCards[i])) * validas[i];
		}
		String lastNum = String.valueOf((12 - count % 11) % 11);
		lastNum = "10".equals(lastNum) ? "x":lastNum;
		return idCard17 + lastNum;
	}
	public static String idcard(String area){
		String format = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			String yyyyMMdd = date(format, "19990101", sdf.format(new Date()));
			String sno = RandomStringUtils.randomNumeric(3);
			return idcard(area, yyyyMMdd, sno);
		} catch (ParseException e) {
			// format has not exception
		}
		return "";
	}

    /**
     * 随机生成身份证号
	 * @return
     */
	public static String idcard(){
		if (AREA_CITY_MAP.size() == 0){
			return idcard("430124");
		}
		int nextInt = RandomUtils.nextInt(0, AREA_CITY_MAP.size());
		return idcard(AREA_CITY_MAP.get(nextInt));
	}
	/**
	 *
	 * 功能:随机生成地址<br>
	 * 创建时间:2016-4-16下午6:19:14<br>
	 * 作者：sanri<br>
	 * 入参说明:<br>
	 * 出参说明：<br>
	 * @return<br>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String address(){
		List<Map> provinceList = (List<Map>) CITY_LIST.get("citylist");
		int provinceIndex =  RandomUtils.nextInt(0,provinceList.size());
		Map provinceEntry = provinceList.get(provinceIndex);
		String province = String.valueOf(provinceEntry.get("p"));

		List<Map> cityList = (List<Map>) provinceEntry.get("c");
		int cityIndex =  RandomUtils.nextInt(0,cityList.size());
		Map cityEntry = cityList.get(cityIndex);
		String city = String.valueOf(cityEntry.get("n"));

		List<Map> areaList = (List<Map>) cityEntry.get("a");
		String area = "";
		if(areaList != null){
			int index =  RandomUtils.nextInt(0,areaList.size());
			Map areaEntry = areaList.get(index);
			area = String.valueOf(areaEntry.get("s"));
		}
		int addressIndex =  RandomUtils.nextInt(0,ADDRESS_LIST.length);
		return province + city + area + ADDRESS_LIST[addressIndex];
	}
	/**
	 *
	 * 功能:随机邮件地址,length 指 用户名长度<br>
	 * 创建时间:2016-9-24下午6:11:54<br>
	 * 作者：sanri<br>
	 */
	public static String email(int length){
		int randomIndex = RandomUtils.nextInt(0, EMAIL_SUFFIX.length);
		return RandomStringUtils.randomAlphanumeric(length)+EMAIL_SUFFIX[randomIndex];
	}

	/**
	 * 随机职业
	 * @return
	 */
	public static String job(){
		int randomIndex = RandomUtils.nextInt(0, JOBS.length);
		return JOBS[randomIndex];
	}
	/**
	 * 生成手机号
	 * @param segment
	 * @return
	 */
	public static String phone(String segment){
		if(StringUtils.isBlank(segment)){
			return phone();
		}
		int length = segment.length();
		int randomLength = 11 - length;
		String randomNumeric = RandomStringUtils.randomNumeric(randomLength);
		return segment+randomNumeric;
	}

	/**
	 * 随机前缀手机号
	 * @return
	 */
	public static String phone(){
		int randomIndex = RandomUtils.nextInt(0,PHONE_SEGMENT.length);
		String segment = PHONE_SEGMENT[randomIndex];
		return phone(segment);
	}

	/**
	 * 生成一个随机日期
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Date date(Date begin ,Date end){
		if(begin == null || end == null || end.before(begin)){
			throw new IllegalArgumentException("请传入正确数据");
		}
		long minus = end.getTime() - begin.getTime();
		long computedMinus = RandomUtils.nextLong(0, minus);
		long computedDateTime = begin.getTime() + computedMinus;
		return new Date(computedDateTime);
	}

	/**
	 * 随机日期
	 * @return
	 */
	public static Date date(){
		return date(new Date(0L),new Date());
	}

	/**
	 * 随机状态量
	 * @param statuses
	 * @return
	 */
	public static int status(int... statuses){
		int index = RandomUtils.nextInt(0, statuses.length);
		return statuses[index];
	}
	public static String status(String...statuses){
		int index = RandomUtils.nextInt(0, statuses.length);
		return statuses[index];
	}

	/**
	 * @Description: 在矩形内随机生成经纬度
	 * @param minLong：最小经度
	 * @param maxLong： 最大经度
	 * @param minLat：最小纬度
	 * @param maxLat：最大纬度
	 */
	public static String randomLongLat(double minLong, double maxLong, double minLat, double maxLat) {
		BigDecimal decimal = new BigDecimal(Math.random() * (maxLong - minLong) + minLong);
		String lon = decimal.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
		decimal = new BigDecimal(Math.random() * (maxLat - minLat) + minLat);
		String lat = decimal.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
		return lon+","+lat;
	}

	/**
	 * 随机几个图片 url
	 * @param count
	 * @return
	 */
	public static String photoURL() {
		// 这个是已经找好的图片地址
		String [] urls = {
				"http://img1.imgtn.bdimg.com/it/u=2346282507,2171850944&fm=26&gp=0.jpg",
				"http://img5.imgtn.bdimg.com/it/u=2945908607,1627227886&fm=26&gp=0.jpg",
				"http://attach.bbs.miui.com/forum/201310/19/235356fyjkkugokokczyo0.jpg",
				"http://attachments.gfan.net.cn/forum/201504/14/075409wgwijxiax3i4wihw.jpg",
				"http://attach.bbs.miui.com/forum/201401/11/145825zn1sxa8anrg11gt1.jpg"
		};
		int index = RandomUtils.nextInt(0, urls.length);
		return urls[index];
	}

	/**
	 * 随机 url
	 * @return
	 */
	public static String url(){
		String [] urls = {
				"https://www.baidu.com/",
				"https://blog.csdn.net/",
				"https://segmentfault.com/",
				"https://www.cnblogs.com/",
				"https://www.jianshu.com/"
		};
		int index = RandomUtils.nextInt(0, urls.length);
		return urls[index];
	}
}
