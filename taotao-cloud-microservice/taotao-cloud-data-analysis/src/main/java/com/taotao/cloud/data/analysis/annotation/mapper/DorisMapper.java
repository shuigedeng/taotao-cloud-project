package com.taotao.cloud.data.analysis.annotation.mapper;

import com.taotao.cloud.data.analysis.annotation.Doris;

@Mapper
@Doris
public interface DorisMapper extends BaseMapper<DataInfo> {

    List<DataInfo> selectAll();

}
