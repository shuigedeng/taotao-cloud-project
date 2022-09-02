package com.taotao.cloud.core.async.worker;


import com.taotao.cloud.core.async.wrapper.WorkerWrapper;

/**
 * 对依赖的wrapper的封装
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-30 13:25:10
 */
public class DependWrapper {

	/**
	 * 取决于包装
	 */
	private WorkerWrapper<?, ?> dependWrapper;
	/**
	 * 必须 是否该依赖必须完成后才能执行自己.<p> 因为存在一个任务，依赖于多个任务，是让这多个任务全部完成后才执行自己，还是某几个执行完毕就可以执行自己 如 1 ---3 2 或
	 * 1---3 2---3 这两种就不一样，上面的就是必须12都完毕，才能3 下面的就是1完毕就可以3
	 */
	private boolean must = true;

	/**
	 * 取决于包装
	 *
	 * @param dependWrapper 取决于包装
	 * @param must          必须
	 * @since 2022-05-30 13:25:10
	 */
	public DependWrapper(WorkerWrapper<?, ?> dependWrapper, boolean must) {
        this.dependWrapper = dependWrapper;
        this.must = must;
    }

	/**
	 * 取决于包装
	 *
	 * @since 2022-05-30 13:25:10
	 */
	public DependWrapper() {
    }

	/**
	 * 得靠包装
	 *
	 * @return {@link WorkerWrapper }<{@link ? }, {@link ? }>
	 * @since 2022-05-30 13:25:10
	 */
	public WorkerWrapper<?, ?> getDependWrapper() {
        return dependWrapper;
    }

	/**
	 * 设置取决于包装
	 *
	 * @param dependWrapper 取决于包装
	 * @since 2022-05-30 13:25:10
	 */
	public void setDependWrapper(WorkerWrapper<?, ?> dependWrapper) {
        this.dependWrapper = dependWrapper;
    }

	/**
	 * 是必须
	 *
	 * @return boolean
	 * @since 2022-05-30 13:25:10
	 */
	public boolean isMust() {
        return must;
    }

	/**
	 * 设置必须
	 *
	 * @param must 必须
	 * @since 2022-05-30 13:25:10
	 */
	public void setMust(boolean must) {
        this.must = must;
    }

	/**
	 * 字符串
	 *
	 * @return {@link String }
	 * @since 2022-05-30 13:25:10
	 */
	@Override
    public String toString() {
        return "DependWrapper{" +
                "dependWrapper=" + dependWrapper +
                ", must=" + must +
                '}';
    }
}
