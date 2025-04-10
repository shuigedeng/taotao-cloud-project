package com.taotao.cloud.ccsr.example.dto;

import com.taotao.cloud.ccsr.client.listener.ConfigData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements ConfigData {
    private String name;
    private Integer age;
}
