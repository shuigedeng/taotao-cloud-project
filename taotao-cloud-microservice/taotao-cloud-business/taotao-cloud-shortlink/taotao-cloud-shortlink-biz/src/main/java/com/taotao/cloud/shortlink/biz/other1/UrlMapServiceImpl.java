package com.taotao.cloud.shortlink.biz.other1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class UrlMapServiceImpl implements UrlMapService {
  /**
   * UrlMap 接口注入
   */
  @Autowired
  private UrlMapDao urlMapDao;

  /**
   * 为长链接创建对应的键值
   * @param longUrl 需要进行短链接 key 编码的长链接
   * @return 短链接的键值
   */
  @Override
  public String encode(String longUrl) {
    UrlMap urlMap = urlMapDao.findFirstByLongUrl(longUrl);

    if (urlMap == null) {
      urlMap = urlMapDao.save(UrlMap.builder()
           .longUrl(longUrl)
           .expireTime(Instant.now().plus(30, ChronoUnit.DAYS))
           .build()
       );
      log.info("create urlMap:{}", urlMap);
     }
    return Base62Utils.idToShortKey(urlMap.getId());
   }

    /**
	   * 短链接重定向开发
	   * @param shortKey 需要进行解码的短链接 Key 值
	   * @return 对应的长链接
	   */
		 @Override
  public Optional<String> decode(String shortKey) {
    long id = Base62Utils.shortKeyToId(shortKey);
    return urlMapDao.findById(id).map(UrlMap::getLongUrl);
   }

}
