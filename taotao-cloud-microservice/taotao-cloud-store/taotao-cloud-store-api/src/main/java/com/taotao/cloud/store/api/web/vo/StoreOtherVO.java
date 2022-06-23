package com.taotao.cloud.store.api.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺其他信息
 * 
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺其他信息")
public class StoreOtherVO {

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司地址")
    private String companyAddress;

    @Schema(description = "公司地址地区")
    private String companyAddressPath;

    @Schema(description = "营业执照电子版")
    private String licencePhoto;

    @Schema(description = "法定经营范围")
    private String scope;

    @Schema(description = "员工总数")
    private Integer employeeNum;
}
