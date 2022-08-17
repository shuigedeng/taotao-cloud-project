
package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.sys.biz.model.entity.system.Visits;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * IVisitsMapper
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-29 08:57:49
 */
public interface IVisitsMapper extends BaseMapper<Visits> {

	@Select("select * FROM visits where create_time between #{time1} and #{time2}")
	List<Visits> findAllVisits(@Param("time1") String time1, @Param("time2") String time2);
}
