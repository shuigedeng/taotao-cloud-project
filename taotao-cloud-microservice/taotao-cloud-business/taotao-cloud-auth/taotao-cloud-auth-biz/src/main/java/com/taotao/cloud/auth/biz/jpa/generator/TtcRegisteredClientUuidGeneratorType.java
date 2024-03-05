
package com.taotao.cloud.auth.biz.jpa.generator;

import com.taotao.cloud.auth.biz.jpa.entity.TtcRegisteredClient;
import com.taotao.cloud.data.jpa.hibernate.identifier.AbstractUuidGenerator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;

import java.lang.reflect.Member;

/**
 * <p>OAuth2RegisteredClient Id 生成器 </p>
 * <p>
 * 指定ID生成器，解决实体ID无法手动设置问题。
 *
 */
public class TtcRegisteredClientUuidGeneratorType extends AbstractUuidGenerator {

    public TtcRegisteredClientUuidGeneratorType(TtcRegisteredClientUuidGenerator config, Member idMember, CustomIdGeneratorCreationContext creationContext) {
        super(idMember);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (ObjectUtils.isEmpty(object)) {
            throw new HibernateException(new NullPointerException());
        }

        TtcRegisteredClient ttcRegisteredClient = (TtcRegisteredClient) object;

        if (StringUtils.isEmpty(ttcRegisteredClient.getId())) {
            return super.generate(session, object);
        } else {
            return ttcRegisteredClient.getId();
        }
    }
}
