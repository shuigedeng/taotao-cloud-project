package com.taotao.cloud.sys.biz.execl;

import java.util.HashMap;
import java.util.Map;

public enum UploadType {
   未知(1,"未知"),
   类型2(2,"类型2"),
   类型1(3,"类型1");

   private int code;
   private String desc;
   private static Map<Integer, UploadType> map = new HashMap<>();


   static {
      for (UploadType value : UploadType.values()) {
         map.put(value.code, value);
      }
   }

   UploadType(int code, String desc) {
      this.code = code;
      this.desc = desc;
   }

   public int getCode() {
      return code;
   }

   public String getDesc() {
      return desc;
   }

   public static UploadType getByCode(Integer code) {
      return map.get(code);
   }
}
