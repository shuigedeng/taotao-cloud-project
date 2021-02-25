package com.taotao.cloud.standalone.system.modules.security.social.github.api;

/**
 * @author huan.fu
 * @since 2018/11/26 - 18:12
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * git hub api operation
 *
 * @author huan.fu
 * @since 2018/11/26 - 18:12
 */
@Slf4j
public class GitHubImpl extends AbstractOAuth2ApiBinding implements GitHub {

	private static final String URL_GET_USRE_INFO = "https://api.github.com/user";
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z", Locale.ENGLISH);

	public GitHubImpl(String accessToken) {
		super(accessToken);
	}

	@Override
	public GitHubUserInfo getUserInfo() {
		Map<String, ?> user = getRestTemplate().getForObject(URL_GET_USRE_INFO, Map.class);
		Long gitHubId = Long.valueOf(String.valueOf(user.get("id")));
		String username = String.valueOf(user.get("login"));
		String name = String.valueOf(user.get("name"));
		String location = user.get("location") != null ? String.valueOf(user.get("location")) : null;
		String company = user.get("company") != null ? String.valueOf(user.get("company")) : null;
		String blog = user.get("blog") != null ? String.valueOf(user.get("blog")) : null;
		String email = user.get("email") != null ? String.valueOf(user.get("email")) : null;
		Date createdDate = toDate(String.valueOf(user.get("created_at")), dateFormat);
		String gravatarId = (String) user.get("gravatar_id");
		String profileImageUrl = gravatarId != null ? "https://secure.gravatar.com/avatar/" + gravatarId : null;
		String avatarUrl = user.get("avatar_url") != null ? String.valueOf(user.get("avatar_url")) : null;
		GitHubUserInfo userInfo = GitHubUserInfo.builder()
				.id(gitHubId)
				.username(username)
				.name(name)
				.location(location)
				.company(company)
				.blog(blog)
				.email(email)
				.profileImageUrl(profileImageUrl)
				.avatarUrl(avatarUrl)
				.createdDate(createdDate)
				.build();
		log.info("github userInfo : [{}]", userInfo);
		return userInfo;
	}

	private Date toDate(String dateString, DateFormat dateFormat) {
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
}
