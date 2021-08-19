/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.taotao.cloud.oss.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.taotao.cloud.oss.core.OssTemplate;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * AWS对外提供服务接口
 *
 * @author lengleng
 * @author 858695266
 * @since 1.0.0
 */
@RestController
@RequestMapping("/oss")
public class OssEndpoint {

	private final OssTemplate template;

	public OssEndpoint(OssTemplate template) {
		this.template = template;
	}

	/**
	 * Bucket Endpoints
	 */
	@PostMapping("/bucket/{bucketName}")
	public Bucket createBucket(@PathVariable String bucketName) {
		template.createBucket(bucketName);
		return template.getBucket(bucketName).get();
	}

	@GetMapping("/bucket")
	public List<Bucket> getBuckets() {
		return template.getAllBuckets();
	}

	@GetMapping("/bucket/{bucketName}")
	public Bucket getBucket(@PathVariable String bucketName) {
		return template.getBucket(bucketName)
				.orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
	}

	@DeleteMapping("/bucket/{bucketName}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteBucket(@PathVariable String bucketName) {
		template.removeBucket(bucketName);
	}

	/**
	 * Object Endpoints
	 */
	@PostMapping("/object/{bucketName}")
	public S3Object createObject(@RequestBody MultipartFile object,
			@PathVariable String bucketName) throws Exception {
		String name = object.getOriginalFilename();

		try {
			template.putObject(bucketName, name, object.getInputStream(), object.getSize(),
					object.getContentType());
			return template.getObjectInfo(bucketName, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/object/{bucketName}/{objectName}")
	public S3Object createObject(@RequestBody MultipartFile object, @PathVariable String bucketName,
			@PathVariable String objectName) throws Exception {

		try {
			template.putObject(bucketName, objectName, object.getInputStream(), object.getSize(),
					object.getContentType());
			return template.getObjectInfo(bucketName, objectName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/object/{bucketName}/{objectName}")
	public List<S3ObjectSummary> filterObject(@PathVariable String bucketName,
			@PathVariable String objectName) {
		return template.getAllObjectsByPrefix(bucketName, objectName, true);
	}

	@GetMapping("/object/{bucketName}/{objectName}/{expires}")
	public Map<String, Object> getObject(@PathVariable String bucketName,
			@PathVariable String objectName,
			@PathVariable Integer expires) {
		Map<String, Object> responseBody = new HashMap<>(8);
		// Put Object info
		responseBody.put("bucket", bucketName);
		responseBody.put("object", objectName);
		responseBody.put("url", template.getObjectURL(bucketName, objectName, expires));
		responseBody.put("expires", expires);
		return responseBody;
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/object/{bucketName}/{objectName}/")
	public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName)
			throws Exception {
		template.removeObject(bucketName, objectName);
	}

}
