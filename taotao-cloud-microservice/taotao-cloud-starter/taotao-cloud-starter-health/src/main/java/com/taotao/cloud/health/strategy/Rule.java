package com.taotao.cloud.health.strategy;

import com.yh.csx.bsf.core.common.PropertyCache;
import com.yh.csx.bsf.core.util.ConvertUtils;
import com.yh.csx.bsf.core.util.LogUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.health.base.Report;
import lombok.*;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: chejiangyi
 * @version: 2019-07-28 11:48
 * 规则引擎
 **/
public class Rule {
    /**
     * 规则
     */
    @Data
    @AllArgsConstructor
    public static class RuleInfo implements Serializable
    {
        private RuleType type;
        private Object value;
        private HitCallBack hitCallBack;

        @Override
        public String toString() {
            return type.tag+value.toString();
        }

        public boolean check(Object checkvalue){
            if(checkvalue == null)
            {    return false;}
            try {
                if (checkvalue instanceof Number) {
                    double checkvaluevalue2 = ((Number) checkvalue).doubleValue();
                    double warnvalue = (ConvertUtils.convert(value, Number.class)).doubleValue();
                    if (type == RuleType.less && checkvaluevalue2 < warnvalue) {
                        return true;
                    } else if (type == RuleType.more && checkvaluevalue2 > warnvalue) {
                        return true;
                    } else if (type == RuleType.equal && checkvaluevalue2 == warnvalue) {
                        return true;
                    }
                } else {
                    String checkvaluevalue2 = checkvalue.toString();
                    String warnvalue = value.toString();
                    if (type == RuleType.equal && checkvaluevalue2 == warnvalue) {
                        return true;
                    } else if (type == RuleType.contain && checkvaluevalue2.contains(warnvalue)) {
                        return true;
                    }
                }
            }
            catch (Exception exp){
                LogUtils.error(RuleInfo.class,"health","check 规则检查出错",exp);
            }
            return false;
        }
    }

    /**
     * 规则分析器
     */
    static public class RulesAnalyzer
    {
        private Map<String,List<RuleInfo>> rules = new HashMap<>();
        private RuleParser ruleParser = new RuleParser();

        public RulesAnalyzer(){
            //订阅配置改变，重新注册规则
            PropertyCache.Default.listenUpdateCache("RulesAnalyzer 动态规则订阅",(map)->{

                for(val e: map.entrySet()){
                    var key = e.getKey();
                    if(StringUtils.startsWithIgnoreCase(key,"bsf.health.strategy.")) {
                        key=key.replace("bsf.health.strategy.","");
                        val rule = rules.get(key);
                        if (rule != null) {
                            registerRules(key, com.yh.csx.bsf.core.util.StringUtils.nullToEmpty(e.getValue()));
                        }
                    }
                }
            });
        }

        public List<RuleInfo> parserRules(String rules) {
            return ruleParser.parser(rules);
        }

        public List<RuleInfo> getRules(String field){
            val item = rules.get(field);
            if(item==null)
            {
                registerRulesByProperties(field);
            }
            return rules.get(field);
        }

        public void registerRules(String field, List<RuleInfo>rules){
//            if(this.rules.containsKey(field)) {
//                this.rules.get(field).addAll(rules);
//
//            }else{
                this.rules.put(field,rules);
//            }
        }
        public void registerRules(String field,String rules){
            registerRules(field,ruleParser.parser(rules));
        }

        public void registerRulesByProperties(String field){
            val value = PropertyUtils.getPropertyCache("bsf.health.strategy."+field,"");
            registerRules(field,value);
        }


        public Report analyse(Report report){
            report.eachReport((fieldname,item)->{
                val rules = this.getRules(fieldname);
                if(rules!=null)
                {
                   for(val ruleInfo: rules)
                   {
                       boolean isWarn = ruleInfo.check(item.getValue());
                       if(isWarn == true){
                           item.setWarn("报警");
                           item.setRule(ruleInfo);
                           if(ruleInfo.getHitCallBack()!=null){
                               try
                               {
                                   ruleInfo.hitCallBack.run(item.getValue());
                               }
                               catch (Exception exp){
                                   LogUtils.error(RulesAnalyzer.class,"health","analyse分析时执行报警回调规则出错",exp);
                               }
                           }
                       }
                   }
                }
                return item;
            });
            return report;
        }
    }
    public static enum RuleType{
        more(">","大于"),
        less("<","小于"),
        equal("=","等于"),
        contain("%","包含");

        private String desc;
        private String tag;

        private RuleType(String tag,String desc){
            this.desc = desc;
            this.tag= tag;
        }
        public static RuleType getRuleType(String tag){
              for(val type : RuleType.values()){
                  if(type.tag.equalsIgnoreCase(tag)){
                      return type;
                  }
              }
              return null;
        }
    }
    /**
     * 规则解析器
     */
    public static class RuleParser
    {
        public List<RuleInfo> parser(String text){
            List<RuleInfo> result = new ArrayList<>();
            try {
                if (text.startsWith("[") && text.endsWith("]")) {
                    text = text.replace("[", "").replace("]", "");
                    String[] rules = text.split(";");
                    for (String r : rules) {
                        val type = RuleType.getRuleType(r.charAt(0) + "");
                        val value = StringUtils.trimLeadingCharacter(r, r.charAt(0));
                        //val numvalue = ConvertUtils.tryConvert(value, Number.class);
                        if (type != null) {
                            result.add(new RuleInfo(type, value, null));
                        }
                    }
                }
            }catch (Exception exp){
                LogUtils.error(RuleParser.class,"health","parser规则解析出错",exp);
            }
            return result;
        }
    }
    /**
     * 命中回调
     * */
    public interface HitCallBack
    {
        void run(Object value);
    }
}
