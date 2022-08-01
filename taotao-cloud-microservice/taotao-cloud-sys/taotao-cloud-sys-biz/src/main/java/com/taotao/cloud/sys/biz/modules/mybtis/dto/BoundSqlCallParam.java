package com.taotao.cloud.sys.biz.modules.mybtis.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BoundSqlCallParam {
   @NotNull
   private String project;
   @NotNull
   private String statementId;
   @NotNull
   private String className;
   @NotNull
   private String classloaderName;
   private JSONObject arg;
   @NotNull
   private String connName;
}
