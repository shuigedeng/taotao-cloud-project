package com.taotao.cloud.data.p6spy.ext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据p6spy官方配置文件，快速生成字段
 */
public class SmartPropertiesFieldsGenerator {

    static final Pattern FIELD_PATTERN = Pattern.compile("^#(\\w+)=.*");

    public static void main(String[] args) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/spy.properties"))) {
            String line;
            Set<String> fieldsSet = new HashSet<>(32, 1);
            while ((line = bufferedReader.readLine()) != null) {
                Matcher matcher = FIELD_PATTERN.matcher(line);
                if (matcher.find()) {
                    String field = matcher.group(1);
                    field = String.format("private String %s;", field);
                    fieldsSet.add(field);
                }
            }
            fieldsSet.forEach(System.out::println);
        }
    }
}
