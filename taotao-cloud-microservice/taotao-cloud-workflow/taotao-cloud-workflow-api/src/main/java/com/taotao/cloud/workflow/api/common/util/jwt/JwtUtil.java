package com.taotao.cloud.workflow.api.common.util.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;

/**
 *
 */
public class JwtUtil {

    /**
     * 获取jwt中的携带的Redis的token
     * @param token
     * @return
     */
    public static String getRealToken(String token) {
        String realToken;
        try {
            SignedJWT sjwt = SignedJWT.parse(token.split(" ")[1]);
            JWTClaimsSet claims = sjwt.getJWTClaimsSet();
            realToken =  String.valueOf(claims.getClaim("token"));
            return realToken;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取jwt中的携带的Redis的token
     * @param token
     * @return
     */
    public static Integer getSingleLogin(String token) {
        int singleLogin;
        try {
            SignedJWT sjwt = SignedJWT.parse(token.split(" ")[1]);
            JWTClaimsSet claims = sjwt.getJWTClaimsSet();
            singleLogin = (int) claims.getClaim("singleLogin");
            return singleLogin;
        } catch (Exception e) {
            return singleLogin = 1;
        }
    }

    /**
     * 获取jwt中的过期时间
     * @param token
     * @return
     */
    public static Date getExp(String token){
        Date date;
        try {
            SignedJWT sjwt = SignedJWT.parse(token.split(" ")[1]);
            JWTClaimsSet claims = sjwt.getJWTClaimsSet();
            date = (Date)claims.getClaim("exp");
            return date;
        } catch (Exception e) {
            return date = null;
        }
    }

    public static void main(String[] args) {
        String test = getRealToken("Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxNDEwMDEiLCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNjA5MDA5NDQxLCJhdXRob3JpdGllcyI6WyJhYSJdLCJqdGkiOiIxYWNkYTI4MS0yYTUxLTRmZGYtYmRlYy03OWFkNmI5NzZmODEiLCJjbGllbnRfaWQiOiJhZG1pbiIsInRva2VuIjoibG9naW5fdG9rZW5fMDNhNDEyNTgwOGU5NGVhYjhlY2I3MzM4OTQ2ZjJhMzgifQ.b1LJ5dWQeI0it7JPP0vAm56Ns-2l-zpi768Z2KhdAODLWyfO640jIz02dNixfnw0_2hgBwqj9Y-1NIMVEZmRPMoRhIOwh6qw4p8b05k8Y3M2KXhdYaQTaw9ZkpR-TFRuVf8_v2bUaUjmnulXRffV3iVAYmcZcXHBrv0938_oJJEIKHmjtlbbOCaKIf6IEPCwmFci8gLCnld6FnVIytg9rMD85AsagwLHs_dNaNavEg3-s5Fi9jov7L2_h940aHPvtwBiCNpPkFIA-hmYb7-ChETmx8yFN3TnJbNX4-wpQ_dJlaNnHTtbt8ztNr-ugXbKGqfkZzWPxn-anqeSjyBUAA");
        System.out.println(test);
    }


}
