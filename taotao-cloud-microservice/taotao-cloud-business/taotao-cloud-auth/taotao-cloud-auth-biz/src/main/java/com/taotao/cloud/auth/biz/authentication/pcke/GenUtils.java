package com.taotao.cloud.auth.biz.authentication.pcke;

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GenUtils {

	public static String genCode() throws NoSuchAlgorithmException {
		// 生成随机字符串
		StringKeyGenerator authorizationCodeGenerator = new Base64StringKeyGenerator(
			Base64.getUrlEncoder().withoutPadding(), 96);
		String codeVerifier = authorizationCodeGenerator.generateKey();
		System.out.println(codeVerifier);
		System.out.println(codeVerifier.length());
		// 加密并再次编码
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
		String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
		System.out.println(codeChallenge);
		return codeChallenge;
	}

/*    public static String t() throws NoSuchAlgorithmException {
        // Dependency: Apache Commons Codec
        // https://commons.apache.org/proper/commons-codec/
        // Import the Base64 class.
        // import org.apache.commons.codec.binary.Base64;
        byte[] bytes = verifier.getBytes("US-ASCII");


        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes,0,bytes.length);
        byte[] digest = md.digest();
        String challenge = Base64.getEncoder().encodeToString(digest);
        System.out.println(challenge);
        return challenge;
    }*/

	public static void main(String[] args) throws NoSuchAlgorithmException {
		genCode();
	}
}
