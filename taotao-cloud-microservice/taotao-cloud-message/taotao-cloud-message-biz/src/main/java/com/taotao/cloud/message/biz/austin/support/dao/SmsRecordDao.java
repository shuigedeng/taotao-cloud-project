package com.taotao.cloud.message.biz.austin.support.dao;


import com.taotao.cloud.message.biz.austin.support.domain.SmsRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * 短信记录的Dao
 * @author 3y
 *
 */
public interface SmsRecordDao extends CrudRepository<SmsRecord, Long> {


}
