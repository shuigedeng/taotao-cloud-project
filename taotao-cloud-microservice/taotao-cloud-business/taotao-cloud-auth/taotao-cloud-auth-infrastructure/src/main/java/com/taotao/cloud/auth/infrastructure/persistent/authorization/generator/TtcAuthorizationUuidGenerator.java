

package com.taotao.cloud.auth.infrastructure.persistent.authorization.generator;

import com.taotao.cloud.auth.infrastructure.persistent.authorizaon.generator.TtcAuthorizationUuidGeneratorType;
import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * <p>TtcAuthorizationUuid 注解 </p>
 *
 */
@IdGeneratorType(TtcAuthorizationUuidGeneratorType.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, METHOD})
public @interface TtcAuthorizationUuidGenerator {
}
