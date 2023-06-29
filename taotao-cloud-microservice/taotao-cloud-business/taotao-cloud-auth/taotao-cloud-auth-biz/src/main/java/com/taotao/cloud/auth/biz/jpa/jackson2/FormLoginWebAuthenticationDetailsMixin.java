
package com.taotao.cloud.auth.biz.jpa.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * <p>Description: FormLoginWebAuthenticationDetailsMixin </p>
 *
 *
 * @date : 2022/4/14 11:03
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonDeserialize(using = FormLoginWebAuthenticationDetailsDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.ANY)
public class FormLoginWebAuthenticationDetailsMixin {

}
