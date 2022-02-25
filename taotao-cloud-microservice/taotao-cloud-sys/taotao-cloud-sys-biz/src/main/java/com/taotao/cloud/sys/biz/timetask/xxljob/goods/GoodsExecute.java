//package com.taotao.cloud.sys.biz.timetask.xxljob.goods;
//
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.date.DateTime;
//import cn.hutool.core.date.DateUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.taotao.cloud.web.timetask.EveryDayExecute;
//import java.util.List;
//import java.util.Map;
//import javax.annotation.Resource;
//import org.springframework.stereotype.Component;
//
///**
// * 商品定时器
// */
//@Component
//public class GoodsExecute implements EveryDayExecute {
//    /**
//     * 会员评价
//     */
//    @Resource
//    private MemberEvaluationMapper memberEvaluationMapper;
//    /**
//     * 商品
//     */
//    @Resource
//    private GoodsMapper goodsMapper;
//
//    /**
//     * 查询已上架的商品的评价数量并赋值
//     */
//    @Override
//    public void execute() {
//
//        //查询上次统计到本次的评价数量
//        List<Map<String, Object>> list = memberEvaluationMapper.memberEvaluationNum(
//                new QueryWrapper<MemberEvaluation>()
//                        .between("create_time", DateUtil.yesterday(), new DateTime()));
//
//        for (Map<String, Object> map : list) {
//            goodsMapper.addGoodsCommentNum(Convert.toInt(map.get("num").toString()), map.get("goods_id").toString());
//        }
//
//    }
//}
