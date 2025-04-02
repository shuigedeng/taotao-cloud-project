package com.taotao.cloud.job.nameserver.module;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ReBalanceInfo {

    List<String> ServerIpList;
    boolean isSplit;
    boolean isChangeServer;
    String subAppName;

}
