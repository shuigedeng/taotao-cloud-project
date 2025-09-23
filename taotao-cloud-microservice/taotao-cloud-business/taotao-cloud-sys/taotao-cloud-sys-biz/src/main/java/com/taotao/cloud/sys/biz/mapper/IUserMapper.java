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

package com.taotao.cloud.sys.biz.mapper;

import com.taotao.cloud.sys.biz.model.entity.system.User;
import com.taotao.boot.data.mybatis.mybatisplus.base.mapper.MpSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * IUserMapper
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/13 22:50
 */
@Repository
public interface IUserMapper extends MpSuperMapper<User, Long> {


    /**
     * create table if not exists sys_person_phone_encrypt
     * (
     *    id bigint auto_increment comment '主键' primary key,
     *    person_id int not null comment '关联人员信息表主键',
     *    phone_key varchar(500) not null comment '手机号码分词密文'
     * )
     * comment '人员的手机号码分词密文映射表';
     *
     * @param personId
     * @param phoneKey
     * @return
     */
    @Update(value = """
              insert into sys_person_phone_encrypt (person_id, phone_key) value(#{personId},#{phoneKey})
            """)
    Integer insertPhoneKeyworkds(Long personId, String phoneKey);


    @Select("""
            select * from sys_person where id in (select person_id from sys_person_phone_encrypt where phone_key like concat('%',#{phoneVal},'%'))
            """)
    List<User> queryByPhoneEncrypt(@Param("phoneVal") String phoneVal);
}
