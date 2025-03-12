package com.taotao.cloud.shortlink.biz.other1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "t_url_map", indexes = {@Index(columnList = "longUrl", unique = true),
	@Index(columnList = "expireTime", unique = false)})
@Data
@Accessors(chain=true)
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
