package com.taotao.cloud.office.excel.annotation;



import com.taotao.cloud.office.excel.core.CellMergeStrategy;
import java.lang.annotation.*;

/**
 * excel 列单元格合并(合并列相同项)
 * <p>
 * 需搭配 {@link CellMergeStrategy} 策略使用
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 20:53:38
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellMerge {

	/**
	 * col index
	 */
	int index() default -1;

}
