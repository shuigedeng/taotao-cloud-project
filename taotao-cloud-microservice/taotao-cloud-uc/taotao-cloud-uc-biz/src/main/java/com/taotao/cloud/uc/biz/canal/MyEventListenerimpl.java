package com.taotao.cloud.uc.biz.canal;

import com.taotao.cloud.canal.abstracts.DeleteOption;
import com.taotao.cloud.canal.abstracts.InsertOption;
import com.taotao.cloud.canal.abstracts.UpdateOption;
import com.taotao.cloud.canal.model.DealCanalEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 实现接口方式
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/31 09:07
 */
@Component
public class MyEventListenerimpl extends DealCanalEventListener {

	@Autowired
	public MyEventListenerimpl(
		@Qualifier("realInsertOptoin") InsertOption insertOption,
		@Qualifier("realDeleteOption") DeleteOption deleteOption,
		@Qualifier("realUpdateOption") UpdateOption updateOption) {
		super(insertOption, deleteOption, updateOption);
	}
}
