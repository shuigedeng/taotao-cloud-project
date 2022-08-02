package com.taotao.cloud.sys.biz.modules.redis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColonCommandReply extends CommandReply {
    public ColonCommandReply() {
        super(":");
    }

    public Map<String,String> parserKeyValue(String replay){
        List<String[]> parser = parser(replay);
        Map<String,String> pairs = new HashMap<>();

        for (String[] line : parser) {
            if(line.length < 2) {
                continue;
            }
            String key = line[0];
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 1; i < line.length ; i++) {
                stringBuffer.append(":").append(line[i]);
            }
            String value = stringBuffer.substring(1);
            pairs.put(key,value);
        }

        return pairs;
    }

    public  static ColonCommandReply colonCommandReply = new ColonCommandReply();
}
