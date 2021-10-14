package com.taotao.cloud.uc.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

/**
 * 重复校验DTO
 *
 * @author shuigedeng
 * @since 2020/5/2 16:40
 */
@Schema(name = "RepeatCheckDTO", description = "重复检查DTO")
public class RepeatCheckDTO {

	@Schema(description = "字段值 邮箱 手机号 用户名", required = true)
	private String fieldVal;

	@Schema(description = "指用户id 主要作用编辑情况过滤自己的校验", required = true)
	private Integer dataId;

	@Override
	public String toString() {
		return "RepeatCheckDTO{" +
			"fieldVal='" + fieldVal + '\'' +
			", dataId=" + dataId +
			'}';
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RepeatCheckDTO that = (RepeatCheckDTO) o;
		return Objects.equals(fieldVal, that.fieldVal) && Objects.equals(dataId,
			that.dataId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldVal, dataId);
	}

	public String getFieldVal() {
		return fieldVal;
	}

	public void setFieldVal(String fieldVal) {
		this.fieldVal = fieldVal;
	}

	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	public RepeatCheckDTO() {
	}

	public RepeatCheckDTO(String fieldVal, Integer dataId) {
		this.fieldVal = fieldVal;
		this.dataId = dataId;
	}

	public static RepeatCheckDTOBuilder builder() {
		return new RepeatCheckDTOBuilder();
	}

	public static final class RepeatCheckDTOBuilder {

		private String fieldVal;
		private Integer dataId;

		private RepeatCheckDTOBuilder() {
		}

		public static RepeatCheckDTOBuilder aRepeatCheckDTO() {
			return new RepeatCheckDTOBuilder();
		}

		public RepeatCheckDTOBuilder fieldVal(String fieldVal) {
			this.fieldVal = fieldVal;
			return this;
		}

		public RepeatCheckDTOBuilder dataId(Integer dataId) {
			this.dataId = dataId;
			return this;
		}

		public RepeatCheckDTO build() {
			RepeatCheckDTO repeatCheckDTO = new RepeatCheckDTO();
			repeatCheckDTO.setFieldVal(fieldVal);
			repeatCheckDTO.setDataId(dataId);
			return repeatCheckDTO;
		}
	}
}
