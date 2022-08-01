package com.taotao.cloud.sys.biz.modules.redis.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandReply {
    private String separator;

    public CommandReply(String separator) {
        this.separator = separator;
    }


    /**
     * 命令行返回解析
     * @param reply
     * @return
     */
    public List<String[]> parser(String reply){
        if(StringUtils.isBlank(reply)){
            return new ArrayList<>();
        }
        List<String[]> itemList = new ArrayList<>();
        String[] lines = StringUtils.split(reply, '\n');
        for (int i = 0; i < lines.length; i++) {
            String line = StringUtils.trim(lines[i]);
            String[] items = StringUtils.split(line, separator);
            String[] trimItems = new String[items.length];
            for (int j = 0; j < items.length; j++) {
                trimItems[j] = StringUtils.trim(items[j]);
            }
            itemList.add(trimItems);
        }
        return itemList;
    }

    public List<Map<String,String>> parserWithHeader(String reply,String... headers){
        String[] lines = StringUtils.split(reply, '\n');
        List<Map<String,String>> itemList = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = StringUtils.trim(lines[i]);
            String[] items = StringUtils.split(line, separator);
            Map<String,String> map = new HashMap<>();itemList.add(map);
            for (int j = 0; j < headers.length; j++) {
                String key = headers[j];
                String value = items[j];
                map.put(key,value);
            }
        }

        return itemList;
    }

    public <T> List<T> parserWithClass(String reply,Class<T> clazz,String... headers){
        String[] lines = StringUtils.split(reply, '\n');
        List<T> list = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = StringUtils.trim(lines[i]);
            String[] items = StringUtils.split(line, separator);
            Object o = ReflectUtils.newInstance(clazz);
            T object = clazz.cast(o);
            list.add(object);
        }
        return list;
    }

    public List<List<Object>> parserWithColumnProcess(String reply,ColumnProcess columnProcess){
        List<List<Object>> result = new ArrayList<>();
        String[] lines = StringUtils.split(reply, '\n');
        for (String line : lines) {
            String[] items = StringUtils.split(StringUtils.trim(line), separator);
            List<Object> row = new ArrayList<>();result.add(row);
            for (int i = 0; i < items.length; i++) {
                Object o = columnProcess.processColumn(i, items[i]);
                row.add(o);
            }
        }
        return result;
    }

    @FunctionalInterface
    public interface ColumnProcess{
        Object processColumn(int index, String column);
    }

    public static CommandReply spaceCommandReply = new CommandReply(" ");
    public static CommandReply colonCommandReply = new CommandReply(":");
}
