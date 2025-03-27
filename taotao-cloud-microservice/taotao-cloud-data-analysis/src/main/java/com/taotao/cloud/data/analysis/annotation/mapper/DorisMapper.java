package com.taotao.cloud.data.analysis.annotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.data.analysis.annotation.Doris;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
@Mapper
@Doris
public interface DorisMapper extends BaseMapper<DataInfo> {

	List<DataInfo> selectAll();

}
