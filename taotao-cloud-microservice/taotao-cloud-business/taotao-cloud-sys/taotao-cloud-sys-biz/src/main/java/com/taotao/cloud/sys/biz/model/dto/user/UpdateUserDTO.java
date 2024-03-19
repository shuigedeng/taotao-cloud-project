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

package com.taotao.cloud.sys.biz.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.*;

/**
 * 用户更新DTO
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户更新DTO")
public class UpdateUserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "昵称不能为空")
    @Max(value = 10, message = "昵称不能超过10个字符")
    private String nickname;

    @Schema(description = "真实用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "真实用户名不能为空")
    @Max(value = 10, message = "真实用户名不能超过10个字符")
    private String username;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "真实用户名不能为空")
    @Pattern(regexp = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$", message = "手机号码不正确")
    private String phone;

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式错误")
    private String email;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "部门ID")
    private Integer deptId;

    @Schema(description = "岗位ID")
    private Integer jobId;

    @Schema(description = "是否锁定用户")
    private Boolean lockFlag;

    @Schema(description = "是否删除用户")
    private Integer delFlag;

    @Schema(description = "角色id列表")
    private List<Integer> roleList;
}
