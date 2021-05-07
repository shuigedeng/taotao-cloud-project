package com.taotao.cloud.oauth2.biz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : zhiyi Date: 2020/2/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {

	// 我们的全局永久唯一凭证是 id, security 想要的也是这个, 只是他定义的名字叫 username
	// security username 对应 id, 切记切记 !!
	private String id;

	private String username;

	private String password;

	private String phone;

	private String avatar;

	private String nickname;

	private String email;

	private Integer deptId;

	private Boolean enabled;

	private Integer[] roles;
}
