package com.taotao.cloud.message.biz.austin.web.vo;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;


/**
 * 上传后成功返回素材的Id
 *
 * @author shuigedeng
 */
@Accessors(chain=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponseVo {
    private String id;
}
