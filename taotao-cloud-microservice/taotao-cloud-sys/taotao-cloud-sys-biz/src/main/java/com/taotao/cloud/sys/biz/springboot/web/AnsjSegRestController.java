package com.taotao.cloud.sys.biz.springboot.web;

import java.util.Collection;
import java.util.List;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.IDCardRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrhx.springboot.aop.ResultBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author duhongming
 *
 */
@Api(value="分词服务",tags="java或js调用")
@RestController
@RequestMapping(value = "/ansjseg")
public class AnsjSegRestController {
	
	@ApiOperation(value = "精准分词", notes = "/to/analysis")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/to/analysis", method = RequestMethod.POST)
	public ResultBean<List<Term>> toAnalysis(
			@ApiParam(required = true, name = "str",value="文章或短句", defaultValue= "洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹") @RequestParam( value = "str") String str) {
		return new ResultBean<List<Term>>(ToAnalysis.parse(str).getTerms());
	}
	
	@ApiOperation(value = "用户自定义词典优先策略的分词", notes = "/dic/analysis")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/dic/analysis", method = RequestMethod.POST)
	public ResultBean<List<Term>> dicAnalysis(
			@ApiParam(required = true, name = "str",value="文章或短句", defaultValue= "洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹") @RequestParam( value = "str") String str) {
		return new ResultBean<List<Term>>(DicAnalysis.parse(str).getTerms());
	}
	
	@ApiOperation(value = "带有新词发现功能的分词", notes = "/nlp/analysis")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/nlp/analysis", method = RequestMethod.POST)
	public ResultBean<List<Term>> nlpAnalysis(
			@ApiParam(required = true, name = "str",value="文章或短句", defaultValue= "洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹") @RequestParam( value = "str") String str) {
		return new ResultBean<List<Term>>(NlpAnalysis.parse(str).getTerms());
	}
	
	@ApiOperation(value = "面向索引的分词", notes = "/index/analysis")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/index/analysis", method = RequestMethod.POST)
	public ResultBean<List<Term>> indexAnalysis(
			@ApiParam(required = true, name = "str",value="文章或短句", defaultValue= "洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹") @RequestParam( value = "str") String str) {
		return new ResultBean<List<Term>>(IndexAnalysis.parse(str).getTerms());
	}
	
	@ApiOperation(value = "最小颗粒度的分词", notes = "/base/analysis")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/base/analysis", method = RequestMethod.POST)
	public ResultBean<List<Term>> baseAnalysis(
			@ApiParam(required = true, name = "str",value="文章或短句", defaultValue= "洁面仪配合洁面深层清洁毛孔 清洁鼻孔面膜碎觉使劲挤才能出一点点皱纹 脸颊毛孔修复的看不见啦 草莓鼻历史遗留问题没辙 脸和脖子差不多颜色的皮肤才是健康的 长期使用安全健康的比同龄人显小五到十岁 28岁的妹子看看你们的鱼尾纹") @RequestParam( value = "str") String str) {
		return new ResultBean<List<Term>>(BaseAnalysis.parse(str).getTerms());
	}
	
	@ApiOperation(value = "关键词抽取", notes = "/keyword/computer")
	@ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value = "/keyword/computer", method = RequestMethod.POST)
	public ResultBean<Collection<Keyword>> keyWordCompuer(
			@ApiParam(required = true, name = "num",value="关键词数量", defaultValue= "5") @RequestParam( value = "num") Integer num,
			@ApiParam(required = true, name = "title",value="标题", defaultValue= "维基解密否认斯诺登接受委内瑞拉庇护") @RequestParam( value = "title") String title,
			@ApiParam(required = true, name = "content",value="内容", defaultValue= "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。") @RequestParam( value = "content") String content) {
		KeyWordComputer kwc = new KeyWordComputer(num);
		return  new ResultBean<Collection<Keyword>>(kwc.computeArticleTfidf(title, content));
	}
	/**
	 * 基于规则的新词发现，身份证号码识别
	 * 
	 * @author ansj
	 * 
	 */
	public ResultBean<List<Term>> iDCardRecognition(Result result) {
		new IDCardRecognition().recognition(result);
		return new ResultBean<List<Term>>(result.getTerms());
	}
	
}
