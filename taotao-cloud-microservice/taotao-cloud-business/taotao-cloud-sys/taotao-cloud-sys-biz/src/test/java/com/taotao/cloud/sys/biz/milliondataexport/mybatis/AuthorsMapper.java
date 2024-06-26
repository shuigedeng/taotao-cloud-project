package com.taotao.cloud.sys.biz.milliondataexport.mybatis;
import java.util.List;

public interface AuthorsMapper {
    List<Authors> selectByExample(AuthorsExample example);

    List<Authors> streamByExample(AuthorsExample example); // 以stream形式从mysql获取数据

	/**
	 * 流式读取数据
	 * @param vo 查询对象
	 * @param order 排序
	 * @param ossVipCustomerBoResultHandler 回调处理
	 */
	void selectForwardOnly(@Param("record") Vo vo, @Param("order") Order order,
						   ResultHandler<Bo> handler);
}
