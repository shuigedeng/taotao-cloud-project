package com.taotao.cloud.auth.biz.authentication.qrcocde.service;

import org.springframework.stereotype.Service;

@Service
public class DefaultQrcodeService implements QrcodeService {

	@Override
	public boolean verifyQrcode(String qrcode) {
		return false;
	}
}
