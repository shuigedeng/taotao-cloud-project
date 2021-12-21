package com.taotao.cloud.oauth2.biz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class IdGenerator {

    public static String generateAuthorizationId(String id) {
        String currentTime = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS").format(LocalDateTime.now());
        return id + currentTime;
    }
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("123456"));
	}

}
