
package com.taotao.cloud.auth.biz.jpa.service;

import com.taotao.cloud.auth.biz.jpa.entity.HerodotusRegisteredClient;
import com.taotao.cloud.auth.biz.jpa.repository.HerodotusRegisteredClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>Description: HerodotusRegisteredClientService </p>
 * <p>
 * 这里命名没有按照统一的习惯，主要是为了防止与 spring-authorization-server 已有类的同名而导致Bean注入失败
 *
 * 
 * @date : 2022/2/25 21:06
 */
@Service
public class HerodotusRegisteredClientService {

	private static final Logger log = LoggerFactory.getLogger(HerodotusRegisteredClientService.class);

	private final HerodotusRegisteredClientRepository registeredClientRepository;

	@Autowired
	public HerodotusRegisteredClientService(HerodotusRegisteredClientRepository registeredClientRepository) {
		this.registeredClientRepository = registeredClientRepository;
	}


	public Optional<HerodotusRegisteredClient> findByClientId(String clientId) {
		Optional<HerodotusRegisteredClient> result = this.registeredClientRepository.findByClientId(clientId);
		log.info("[Herodotus] |- HerodotusRegisteredClient Service findByClientId.");
		return result;
	}

	public void save(HerodotusRegisteredClient entity) {
		registeredClientRepository.save(entity);

	}

	public HerodotusRegisteredClient findById(String id) {
		return registeredClientRepository.findById(id).get();
	}

	public void deleteById(String id) {
		registeredClientRepository.deleteById(id);
	}
}
