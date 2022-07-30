package com.taotao.cloud.office.docx4j.input.builder;

import com.taotao.cloud.sys.biz.support.docx4j.input.builder.sheet.CellMeta;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

/**
 * 测试数据
 */
public class Person {
    @CellMeta(name = "姓名", index = 0)
    @Size(min = 1, max = 8, message = "姓名长度需要在{min}到{max}之间")
    String name;
    @Range(min = 1, max = 200, message = "年龄需要在{min}到{max}之间")
    @CellMeta(name = "年龄", index = 1, message = "应该为整数")
    Integer age;
    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "[男|女]", message = "性别只能为男或女")
    @CellMeta(name = "性别", index = 2)
    String sex;
    @CellMeta(name = "收入", index = 3, message = "应该为小数")
    BigDecimal income;
    @CellMeta(name = "出生日期", index = 4, message = "应该为yyyy-MM-dd格式")
    LocalDate birthDate;
    @CellMeta(name = "登录时间", index = 5, message = "应该为yyyy-MM-dd HH:mm:ss格式")
    LocalDateTime loginDatetime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDateTime getLoginDatetime() {
		return loginDatetime;
	}

	public void setLoginDatetime(LocalDateTime loginDatetime) {
		this.loginDatetime = loginDatetime;
	}
}
