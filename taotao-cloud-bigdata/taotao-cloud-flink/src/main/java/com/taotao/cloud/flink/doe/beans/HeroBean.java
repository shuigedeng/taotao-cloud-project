/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.flink.doe.beans;

/**
 * @since: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 * {"id":1,"name":"瞎子","combatValue":99.90}
 * {"id":2,"name":"拥杰","combatValue":9.90}
 * {"id":3,"name":"秃星哥,"combatValue":19.90}
 */
public class HeroBean {
    private int id;
    private String name;
    private double combatValue;

    public HeroBean() {}

    public HeroBean(int id, String name, double combatValue) {
        this.id = id;
        this.name = name;
        this.combatValue = combatValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCombatValue() {
        return combatValue;
    }

    public void setCombatValue(double combatValue) {
        this.combatValue = combatValue;
    }
}
