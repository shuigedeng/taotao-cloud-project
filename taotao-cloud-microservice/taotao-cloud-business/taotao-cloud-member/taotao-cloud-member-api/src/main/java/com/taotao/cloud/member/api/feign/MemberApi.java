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

package com.taotao.cloud.member.api.feign;

 import com.taotao.boot.common.constant.ServiceNameConstants;
import com.taotao.boot.common.model.BaseSecurityUser;
import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.member.api.feign.fallback.MemberApiFallback;
import com.taotao.cloud.member.api.feign.response.MemberApiResponse;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:54
 */
@FeignClient(value = ServiceNameConstants.TAOTAO_CLOUD_MEMBER, fallbackFactory = MemberApiFallback.class)
public interface MemberApi {

    /**
     * 通过用户名查询用户包括角色权限等
     *
     * @param nicknameOrUserNameOrPhoneOrEmail 用户名
     * @return 用户信息
     * @since 2020/4/29 17:48
     */
    @GetMapping(value = "/member/feign/info/security")
	BaseSecurityUser getMemberSecurityUser(String nicknameOrUserNameOrPhoneOrEmail);

    /**
     * 根据id查询会员信息
     *
     * @param id id
     * @return 会员信息
     * @since 2020/11/20 下午4:10
     */
    @GetMapping("/member/feign/info/id/{id:[0-9]*}")
	MemberApiResponse findMemberById(@PathVariable(value = "id") Long id);

    /**
     * 更新成员点
     *
     * @param payPoint 支付点
     * @param name 名字
     * @param memberId 成员身份
     * @param s 年代
     * @return {@link Result }<{@link Boolean }>
     * @since 2022-04-25 16:41:42
     */
    @GetMapping(value = "/member/feign/updateMemberPoint")
    Boolean updateMemberPoint(
            @RequestParam Long payPoint,
            @RequestParam String name,
            @RequestParam Long memberId,
            @RequestParam String s);

    @GetMapping(value = "/member/feign/username")
	MemberApiResponse findByUsername(@RequestParam String username);

    @GetMapping(value = "/member/feign/memberId")
	MemberApiResponse getById(@RequestParam Long memberId);

    /**
     * new LambdaUpdateWrapper<Member>() .eq(Member::getId, member.getId())
     * .set(Member::getHaveStore, true) .set(Member::getStoreId, store.getId())
     */
    @GetMapping(value = "/member/feign/memberId/storeId")
    Boolean update(@RequestParam Long memberId, @RequestParam Long storeId);

    @GetMapping(value = "/member/feign/updateById")
    Boolean updateById(@RequestParam MemberApiResponse member);

    @GetMapping(value = "/member/feign/listFieldsByMemberIds")
    List<Map<String, Object>> listFieldsByMemberIds(@RequestParam String s, @RequestParam List<String> ids);
}
