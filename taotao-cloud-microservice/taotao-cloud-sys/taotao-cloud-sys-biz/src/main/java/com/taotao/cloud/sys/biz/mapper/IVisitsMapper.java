
package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.sys.biz.entity.Visits;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IVisitsMapper extends BaseMapper<Visits> {

	@Select("select * FROM visits where create_time between #{time1} and #{time2}")
	List<Visits> findAllVisits(@Param("time1") String time1, @Param("time2") String time2);
}
