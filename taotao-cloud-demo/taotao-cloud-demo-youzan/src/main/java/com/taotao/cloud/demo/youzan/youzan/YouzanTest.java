/**
 * Project Name: my-projects
 * Package Name: com.taotao.cloud.demo.youzan.youzan
 * Date: 2020/4/29 10:27
 * Author: dengtao
 */
package com.taotao.cloud.demo.youzan.youzan;

import com.youzan.cloud.open.sdk.common.exception.SDKException;
import com.youzan.cloud.open.sdk.core.client.auth.Token;
import com.youzan.cloud.open.sdk.core.client.core.YouZanClient;
import com.youzan.cloud.open.sdk.core.oauth.model.OAuthToken;
import com.youzan.cloud.open.sdk.core.oauth.token.TokenParameter;
import com.youzan.cloud.open.sdk.gen.v3_0_0.api.YouzanScrmCustomerGet;
import com.youzan.cloud.open.sdk.gen.v3_0_0.api.YouzanScrmCustomerUpdate;
import com.youzan.cloud.open.sdk.gen.v3_0_0.model.YouzanScrmCustomerGetParams;
import com.youzan.cloud.open.sdk.gen.v3_0_0.model.YouzanScrmCustomerGetResult;
import com.youzan.cloud.open.sdk.gen.v3_0_0.model.YouzanScrmCustomerUpdateParams;
import com.youzan.cloud.open.sdk.gen.v3_0_0.model.YouzanScrmCustomerUpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <br>
 *
 * @author dengtao
 * @version v1.0.0
 * @date 2020/4/29 10:27
 */
@Component
public class YouzanTest {
    @Autowired
    private YouZanClient youZanClient;

    private Token getAccessToken() throws SDKException {
        TokenParameter parameter = new TokenParameter.SelfBuilder()
//                .clientId("bb307e982e814fe8a9")
                .clientId("7b249964e20714b639")
//                .clientSecret("df571f898d354210fae0d7cde17de046")
                .clientSecret("ec5495e7d79b7737cf274a45f5de01a2")
                .grantId("44590261")
                .build();

        OAuthToken oAuthToken = youZanClient.getOAuthToken(parameter);
        String accessToken = oAuthToken.getAccessToken();
        return new Token(accessToken);
    }


    public void getYouZanUserInfo() throws SDKException {
        YouzanScrmCustomerGet youzanScrmCustomerGet = new YouzanScrmCustomerGet();
        YouzanScrmCustomerGetParams youzanScrmCustomerGetParams = new YouzanScrmCustomerGetParams();
        youzanScrmCustomerGetParams.setMobile("15730445330");
        youzanScrmCustomerGet.setAPIParams(youzanScrmCustomerGetParams);

        YouzanScrmCustomerGetResult result = youZanClient.invoke(youzanScrmCustomerGet, getAccessToken(), YouzanScrmCustomerGetResult.class);
        System.out.println(result);
    }

    public void updataYouzanUserInfo() throws SDKException {
        YouzanScrmCustomerUpdateParams params = new YouzanScrmCustomerUpdateParams();
        YouzanScrmCustomerUpdateParams.YouzanScrmCustomerUpdateParamsAccount account =
                new YouzanScrmCustomerUpdateParams.YouzanScrmCustomerUpdateParamsAccount();
        account.setAccountType("Mobile");
        account.setAccountId("13272939349");
        params.setAccount(account);
        YouzanScrmCustomerUpdateParams.YouzanScrmCustomerUpdateParamsCustomerupdate customerupdate =
                new YouzanScrmCustomerUpdateParams.YouzanScrmCustomerUpdateParamsCustomerupdate();
        customerupdate.setBirthday("2014-01-04 00:00:00");
        params.setCustomerUpdate(customerupdate);

        YouzanScrmCustomerUpdate youzanScrmCustomerUpdate = new YouzanScrmCustomerUpdate();
        youzanScrmCustomerUpdate.setAPIParams(params);

        YouzanScrmCustomerUpdateResult result = youZanClient.invoke(youzanScrmCustomerUpdate, getAccessToken(), YouzanScrmCustomerUpdateResult.class);
        System.out.println(result);
    }
}
