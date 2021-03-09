package com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {


    public static String toJSON(Object src){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
