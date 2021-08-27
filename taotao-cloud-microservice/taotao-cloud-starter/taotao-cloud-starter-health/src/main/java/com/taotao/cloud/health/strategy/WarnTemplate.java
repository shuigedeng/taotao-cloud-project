package com.taotao.cloud.health.strategy;

/**
 * @author: chejiangyi
 * @version: 2019-07-28 11:47
 **/

import java.util.HashMap;

/**
 * 报警模板
 */
public class WarnTemplate extends HashMap<String,String> {
    public static WarnTemplate Default = new WarnTemplate()
            .register("","参数:{name}({desc}),命中规则:{rule},当前值：{value}");
    public WarnTemplate register(String filed,String template){
        if(!this.containsKey(filed)) {
            this.put(filed, template);
        }
        else{
            this.replace(filed,template);
        }
        return this;
    }
    public String getTemplate(String filed){
        if(!this.containsKey(filed)) {
            return this.get("");
        }
        else{
            return this.get(filed);
        }
    }
    public String getWarnConent(String filedName,String filedDesc,Object value, Rule.RuleInfo rule){
        return getTemplate(filedName).replace("{value}",value.toString())
                .replace("{rule}",rule.toString())
                .replace("{name}",filedName)
                .replace("{desc}",filedDesc);
    }
}
