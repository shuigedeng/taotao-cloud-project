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

package com.taotao.cloud.im.biz.platform.common.utils.redis;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/** GeoHash工具类 */
@Component
public class GeoHashUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * 将指定的地理空间位置（纬度、经度、名称）添加到指定的key中。
     * @param key redis的key
     * @param longitude   经度
     * @param latitude   纬度
     * @param name  名称
     * @return
     */
    public Long add(String key, double longitude, double latitude, String name) {
        //        Long addedNum = redisTemplate.opsForGeo().add("city", new Point(121.47, 31.23),
        // "上海");
        //        Long addedNum = redisTemplate.opsForGeo().add("city", new Point(113.27, 23.13),
        // "广州");
        return redisTemplate.opsForGeo().add(key, new Point(longitude, latitude), name);
    }

    /***
     * 将指定的地理空间位置（纬度、经度、名称）添加到指定的key中。
     * @param key redis的key
     * @param map 名称 - 经度 - 纬度
     * @return
     */
    public Long add(String key, Map<String, Point> map) {
        return redisTemplate.opsForGeo().add(key, map);
    }

    /***
     * 将指定的地理空间移除。
     * @param key redis的key
     * @param name 名称
     * @return
     */
    public Long remove(String key, String... name) {
        return redisTemplate.opsForGeo().remove(key, name);
    }

    /***
     * 将指定的地理空间移除。
     * @param key redis的key
     * @param nameList 名称集合
     * @return
     */
    public Long remove(String key, List<String> nameList) {
        return redisTemplate.opsForGeo().remove(key, nameList.toArray());
    }

    /***
     * 从key里返回所有给定位置元素的位置（经度和纬度）。
     * @param key redis的key
     * @param name  名称
     */
    public List<Point> get(String key, String... name) {
        List<Point> points = redisTemplate.opsForGeo().position(key, name); // params: key, 地方名称...
        return points;
    }

    /***
     * 从key里返回所有给定位置元素的位置（经度和纬度）。
     * @param key redis的key
     * @param nameList  名称的集合
     */
    public List<Point> get(String key, List<String> nameList) {
        List<Point> points = redisTemplate.opsForGeo().position(key, nameList.toArray());
        return points;
    }

    /***
     * 返回两个给定位置之间的距离。
     * @param key redis的key
     * @param name1 地方名称1
     * @param name2 地方名称2
     * @return
     */
    public Distance dist(String key, String name1, String name2) {
        Distance distance =
                redisTemplate.opsForGeo().distance(key, name1, name2, RedisGeoCommands.DistanceUnit.KILOMETERS);
        return distance;
    }

    /***
     * 以给定的城市为中心， 返回键包含的位置元素当中， 与中心的距离不超过给定最大距离的所有位置元素，并给出所有位置元素与中心的平均距离。
     * @param key redis的key
     * @param name 名称
     * @param distance 距离
     * @param count 人数
     * @return
     */
    public List<GeoResult<GeoVo>> radius(String key, String name, Integer distance, Integer count) {
        Distance distances = new Distance(distance, Metrics.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()
                .includeCoordinates()
                .sortAscending()
                .limit(count);
        return redisTemplate.opsForGeo().radius(key, name, distances, args).getContent();
    }

    /***
     * 以给定的经纬度为中心， 返回键包含的位置元素当中， 与中心的距离不超过给定最大距离的所有位置元素，并给出所有位置元素与中心的平均距离。
     * @param key redis的key
     * @param longitude 经度
     * @param latitude 纬度
     * @param distance 距离
     * @param count 人数
     * @return
     */
    public List<GeoResult<GeoVo>> radius(
            String key, double longitude, double latitude, Integer distance, Integer count) {
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(distance, Metrics.KILOMETERS));
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()
                .includeCoordinates()
                .sortAscending()
                .limit(count);
        return redisTemplate.opsForGeo().radius(key, circle, args).getContent();
    }

    /***
     * 返回一个或多个位置元素的 Geohash 表示
     * @param key redis的key
     * @param name  名称的集合
     */
    public List<String> hash(String key, String... name) {
        List<String> results = redisTemplate.opsForGeo().hash(key, name);
        return results;
    }

    /***
     * 返回一个或多个位置元素的 Geohash 表示
     * @param key redis的key
     * @param nameList  名称的集合
     */
    public List<String> hash(String key, List<String> nameList) {
        List<String> results = redisTemplate.opsForGeo().hash(key, nameList.toArray());
        return results;
    }
}
