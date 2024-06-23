package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.other1;

import org.springframework.data.repository.CrudRepository;

public interface UrlMapDao extends CrudRepository<UrlMap, Long> {
  /**
   * 通过长链接主键 查找 UrlMap 实体类
   * @param longUrl
   * @return
   */
  UrlMap findFirstByLongUrl(String longUrl);
}
