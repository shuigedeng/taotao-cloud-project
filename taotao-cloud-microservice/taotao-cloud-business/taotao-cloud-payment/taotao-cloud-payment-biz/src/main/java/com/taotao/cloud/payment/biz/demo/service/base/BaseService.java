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

package com.taotao.cloud.payment.biz.demo.service.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * 通用增删改查service
 *
 * @action
 * @author YunGouOS技术部-029
 * @time 2021年2月19日 下午14:30:02
 */
public abstract class BaseService<T> {

    @Autowired
    private Mapper<T> mapper;

    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    public T selectById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<T> selectAll() {
        return mapper.select(null);
    }

    /**
     * 根据条件查询一条数据，如果有多条数据会抛出异常
     *
     * @param record
     * @return
     */
    public T selectOne(T record) {
        return mapper.selectOne(record);
    }

    /**
     * 根据条件查询数据列表
     *
     * @param record
     * @return
     */
    public List<T> selectListByWhere(T record) {
        return mapper.select(record);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param rows
     * @param record
     * @return
     */
    public PageInfo<T> selectPageListByWhere(Integer page, Integer rows, T record) {
        // 设置分页条件
        PageHelper.startPage(page, rows);
        List<T> list = this.selectListByWhere(record);
        return new PageInfo<T>(list);
    }

    /**
     * 新增数据，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer save(T record) {
        return mapper.insert(record);
    }

    /**
     * 新增数据，使用不为null的字段，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer saveSelective(T record) {
        return mapper.insertSelective(record);
    }

    /**
     * 修改数据，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer update(T record) {
        return mapper.updateByPrimaryKey(record);
    }

    /**
     * 修改数据，使用不为null的字段，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer updateSelective(T record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 修改数据，根据指定条件使用不为null的字段，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer updateSelective(T record, Object example) {
        return mapper.updateByExampleSelective(record, example);
    }

    /**
     * 修改数据，null值也会被更新，返回成功的条数
     *
     * @param record
     * @return
     */
    public Integer updateByExample(T record, Object example) {
        return mapper.updateByExample(record, example);
    }

    /**
     * 批量修改
     *
     * @author YunGouOS
     * @since 2020年1月13日 下午6:37:09
     */
    public Integer updateByIds(Class<T> clazz, T record, String property, List<Object> values) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, values);
        return mapper.updateByExampleSelective(record, example);
    }

    /**
     * 根据id删除数据
     *
     * @param id
     * @return
     */
    public Integer deleteById(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除
     *
     * @param clazz
     * @param property
     * @param values
     * @return
     */
    public Integer deleteByIds(Class<T> clazz, String property, List<Object> values) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, values);
        return mapper.deleteByExample(example);
    }

    /**
     * 根据条件做删除
     *
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record) {
        return mapper.delete(record);
    }

    public Integer deleteByWhere(Example example) {
        return mapper.deleteByExample(example);
    }

    /**
     * 查询指定条件的数量
     *
     * @author YunGouOS
     * @since 2019年2月20日 下午6:52:14
     */
    public Integer selectCount(T record) {
        return mapper.selectCount(record);
    }

    public List<T> selectByExample(Example example) {
        return mapper.selectByExample(example);
    }

    public T selectOneByExample(Example example) {
        return mapper.selectOneByExample(example);
    }

    public Integer selectCountByExample(Example example) {
        return mapper.selectCountByExample(example);
    }

    public PageInfo<T> selectPageByExample(Integer page, Integer rows, Example example) {
        // 设置分页条件
        PageHelper.startPage(page, rows);
        List<T> list = mapper.selectByExample(example);
        return new PageInfo<T>(list);
    }
}
