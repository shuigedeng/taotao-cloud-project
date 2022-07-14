package com.taotao.cloud.workflow.biz.engine.model.flowtask;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class PaginationFlowTask extends PaginationTime {
  /**所属流程**/
  private String flowId;
  /**所属分类**/
  private String flowCategory;
  private String creatorUserId;
  private Integer status;
  @TagModelProperty(hidden = true)
  @JsonIgnore
  private Integer isBatch;
  private String nodeCode;
}
