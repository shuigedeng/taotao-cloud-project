package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.other1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "t_url_map", indexes = {@Index(columnList = "longUrl", unique = true),
	@Index(columnList = "expireTime", unique = false)})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlMap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String longUrl;

	private Instant expireTime;

	@CreationTimestamp
	private Instant creationTime;

}
