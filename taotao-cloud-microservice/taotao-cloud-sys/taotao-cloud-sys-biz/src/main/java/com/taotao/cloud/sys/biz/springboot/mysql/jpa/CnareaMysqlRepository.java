package com.taotao.cloud.sys.biz.springboot.mysql.jpa;

import com.hrhx.springboot.domain.Cnarea;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 
 * @author duhongming
 *
 */
//EhCache（第二步）：在数据访问接口中，增加缓存配置注解
@CacheConfig(cacheNames = "cnareas")
public interface CnareaMysqlRepository extends JpaRepository<Cnarea,Integer>{
	/**
	 * 获取省级信息
	 * @return
	 * @throws Exception
	 */
	@Cacheable
	List<Cnarea> findByLevel(Integer level);
	/**
	 * 根据父级别获取相应区域级别信息
	 * @param level
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@Cacheable
	List<Cnarea> findByLevelAndParentCode(Integer level,String code);
}
