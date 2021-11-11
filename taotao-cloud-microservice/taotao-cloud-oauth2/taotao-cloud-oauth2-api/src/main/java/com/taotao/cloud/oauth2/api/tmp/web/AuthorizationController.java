package com.taotao.cloud.oauth2.api.tmp.web;

import com.taotao.cloud.oauth2.api.tmp.AuthUserOauth2;
import com.taotao.cloud.oauth2.api.tmp.social.SocialDetailsService;
import com.taotao.cloud.oauth2.api.tmp.social.user.GitHubOAuth2User;
import com.taotao.cloud.oauth2.api.tmp.social.user.GiteeOAuth2User;
import com.taotao.cloud.oauth2.api.tmp.social.user.QQOAuth2User;
import com.taotao.cloud.oauth2.api.tmp.social.user.UserDetailOAuthUser;
import com.taotao.cloud.oauth2.api.tmp.social.user.WechatOAuth2User;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**

 * @since 0.0.1
 */
@Controller
public class AuthorizationController {

    private final SocialDetailsService socialDetailsService;

    private final OAuth2AuthorizedClientService authorizedClientService;

	public AuthorizationController(
		SocialDetailsService socialDetailsService,
		OAuth2AuthorizedClientService authorizedClientService) {
		this.socialDetailsService = socialDetailsService;
		this.authorizedClientService = authorizedClientService;
	}

	@GetMapping(value = "/loginPage")
    public String login(Model model) {
        // 已经登录不能再进入登录界面


        Map<String, AuthUserOauth2> socials = socialDetailsService.getSocials(true);
        model.addAttribute("AuthUserOauth2", socials);
        return "login";
    }



    @GetMapping(value = "/resource/ids")
    @ResponseBody
    public String[] getClients() {
        return new String[]{"mall"};
    }

	@GetMapping(value = "/test")
	@ResponseBody
	public String[] test() {
		return new String[]{"mall"};
	}


	@GetMapping(value = "/")
    public String authorizationCodeGrant(Model model) {
        Map<String, AuthUserOauth2> socials = socialDetailsService.getSocials(true);
        model.addAttribute("AuthUserOauth2", socials);
        return "index";
    }


    @PostMapping("/oauth2/unbind/{registrationId}/{principalName}")
    public String unbind(@PathVariable("registrationId") String registrationId,
                         @PathVariable("principalName") String principalName) {

        socialDetailsService.unbindSocial(registrationId, principalName);
        return "redirect:/";
    }

    // todo 这里是否有更加优雅的方式
    @GetMapping(value = "/oauth2/bind/" + GiteeOAuth2User.TYPE)
    public String bindGitee(@RegisteredOAuth2AuthorizedClient(GiteeOAuth2User.TYPE) OAuth2AuthorizedClient authorizedClient) {
        return doBind(GiteeOAuth2User.TYPE, authorizedClient);
    }

    @GetMapping(value = "/oauth2/bind/" + GitHubOAuth2User.TYPE)
    public String bindGithub(@RegisteredOAuth2AuthorizedClient(GitHubOAuth2User.TYPE) OAuth2AuthorizedClient authorizedClient) {
        return doBind(GitHubOAuth2User.TYPE, authorizedClient);
    }

    @GetMapping(value = "/oauth2/bind/" + WechatOAuth2User.TYPE)
    public String bindWechat(@RegisteredOAuth2AuthorizedClient(WechatOAuth2User.TYPE) OAuth2AuthorizedClient authorizedClient) {
        return doBind(WechatOAuth2User.TYPE, authorizedClient);
    }

    @GetMapping(value = "/oauth2/bind/" + QQOAuth2User.TYPE)
    public String bindQQ(@RegisteredOAuth2AuthorizedClient(QQOAuth2User.TYPE) OAuth2AuthorizedClient authorizedClient) {
        return doBind(QQOAuth2User.TYPE, authorizedClient);
    }

    private String doBind(String registrationId, OAuth2AuthorizedClient authorizedClient) {
        authorizedClientService.removeAuthorizedClient(registrationId, authorizedClient.getPrincipalName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UserDetailOAuthUser) {
            UserDetailOAuthUser userDetailOAuthUser = (UserDetailOAuthUser) authentication.getPrincipal();
            socialDetailsService.bindSocial(userDetailOAuthUser.getCustomOAuth2User(),
                    registrationId,
                    authentication.getName()
            );
        } else {
            // fix bug:
            // 可能存在当前有存在 authorizedClient 过,再使用表单登录,或者其他登录
            // 这里的上下文 getPrincipal 将是 AuthCustomUser 或者其他, 故这里先删除,重新登录以下就好
            // 没有 authorizedClient 的时候, 会走登录流程, 所以再次回来时,就是 UserDetailOAuthUser 的了
            return "redirect:/oauth2/bind/" + registrationId;
        }
        return "redirect:/";
    }
}
