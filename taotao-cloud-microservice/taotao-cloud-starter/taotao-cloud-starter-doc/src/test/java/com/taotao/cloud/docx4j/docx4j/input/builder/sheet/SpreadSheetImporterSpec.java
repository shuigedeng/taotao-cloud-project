package com.taotao.cloud.docx4j.docx4j.input.builder.sheet;

import com.taotao.cloud.sys.biz.docx4j.input.FileUtil;
import com.taotao.cloud.sys.biz.docx4j.input.builder.Person;
import com.taotao.cloud.sys.biz.support.docx4j.input.builder.sheet.ImportResult;
import com.taotao.cloud.sys.biz.support.docx4j.input.builder.sheet.SpreadSheetImporter;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

/**
 * {@link SpreadSheetImporter}单元测试
 *
 */
public class SpreadSheetImporterSpec {

	@Test
	public void xls() throws FileNotFoundException {
		ImportResult<Person> resolve =
			SpreadSheetImporter.create(new FileInputStream(
					FileUtil.brotherPath(this.getClass(), "/2003.xls")))
				// 表头行数
				.skip(1)
				// sheet索引
				.sheet(0)
				// 行记录对应数据类型
				.resolve(Person.class);
		// 是否存在校验不通过的数据
		System.out.println(resolve.hasInvalid());
		// 所有非法数据行及错误信息
		System.out.println(resolve.getValidRecords());
		// 所有有效数据
		System.out.println(resolve.getInvalidRecordMessage());
		// 前端需要展示数据
		System.out.println(resolve.getSummary());
	}

	@Test
	public void xlsx() throws FileNotFoundException {
		ImportResult<Person> resolve =
			SpreadSheetImporter.create(
					new FileInputStream(FileUtil.brotherPath(this.getClass(), "/2010.xlsx")))
				.skip(1)
				.sheet(0)
				.resolve(Person.class);
		// 是否存在校验不通过的数据
		System.out.println(resolve.hasInvalid());
		// 所有非法数据行及错误信息
		System.out.println(resolve.getValidRecords());
		// 所有有效数据
		System.out.println(resolve.getInvalidRecordMessage());
		// 前端需要展示数据
		System.out.println(resolve.getSummary());
	}

	@Test
	public void repeated() throws FileNotFoundException {
		ImportResult<Person> resolve =
			SpreadSheetImporter.create(
					new FileInputStream(FileUtil.brotherPath(this.getClass(), "/repeated.xlsx")))
				// 表头行数
				.skip(1)
				// 快速失败
				.failFast(true)
				// sheet索引
				.sheet(0)
				// 行记录对应数据类型
				.resolve(Person.class)
				// 条件去除
				.remove((t, m) -> {
					if (t.getAge() > 20) {
						m.add("年龄太大");
					}
				})
				// 快速去重
				.removeIfRepeated(Person::getName, "姓名重复");
		// 是否存在校验不通过的数据
		System.out.println(resolve.hasInvalid());
		// 跳过行
		System.out.println(resolve.getSkip());
		// 空行
		System.out.println(resolve.getEmpty());
		// 所有非法数据行及错误信息
		System.out.println(resolve.getValidRecords());
		// 所有有效数据
		System.out.println(resolve.getInvalidRecordMessage());
		// 前端需要展示数据
		System.out.println(resolve.getSummary());
	}

	@Test
	public void usage() throws FileNotFoundException {
		ImportResult<Person> result = SpreadSheetImporter.create(
				new FileInputStream(FileUtil.brotherPath(this.getClass(), "/repeated.xlsx")))
			// 表头行数
			.skip(1)
			// 快速失败
			.failFast(true)
			// sheet索引
			.sheet(0)
			// 行记录对应数据类型
			.resolve(Person.class)
			// 条件去除
			.remove((t, m) -> {
				if (t.getAge() > 20) {
					m.add("年龄太大");
				}

				if (BigDecimal.ONE.compareTo(t.getIncome()) >= 0) {
					m.add("收入太少");
				}
			})
			// 快速去重
			.removeIfRepeated(Person::getName, "姓名重复")
			// 仅当存在有效数据就执行
			//.onValid()
			// 当且仅当所有数据有效 且有效数据非空才执行
			.onValid(list -> {
				// 对有效数据进行处理 map的key为行索引 value为行数据
				System.out.println(list.size());
				// 有效数据
				System.out.println(list);
			});
		System.out.println(result.getSummary());
		System.out.println(result.getDetail());
	}
}
