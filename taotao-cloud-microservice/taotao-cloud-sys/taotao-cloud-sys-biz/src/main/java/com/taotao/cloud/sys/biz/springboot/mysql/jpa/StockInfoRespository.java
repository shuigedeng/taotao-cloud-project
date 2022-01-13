package com.taotao.cloud.sys.biz.springboot.mysql.jpa;

import com.hrhx.springboot.domain.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author duhongming
 * @version 1.0
 * @description TODO
 * @date 2020/3/12 10:49
 */
public interface StockInfoRespository extends JpaRepository<StockInfo, String> {
    /**
     * 获取没有公司数据
     * @return
     * @throws Exception
     */
    List<StockInfo> findByIpoDate(Integer ipoDate);
}
