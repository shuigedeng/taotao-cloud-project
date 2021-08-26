package com.taotao.cloud.pay.builders;


import com.taotao.cloud.pay.PayBuilder;
import com.taotao.cloud.pay.configurers.PayMessageConfigurer;
import com.taotao.cloud.pay.merchant.MerchantDetailsService;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author egan
 * <pre>
 *         email egzosn@gmail.com
 *         date  2019/5/6 19:36.
 *         </pre>
 */
public class MerchantDetailsServiceBuilder implements PayBuilder<MerchantDetailsService> {

	protected PayMessageConfigurer configurer;

	public static final InMemoryMerchantDetailsServiceBuilder inMemory() {
		return new InMemoryMerchantDetailsServiceBuilder();
	}

	public static final JdbcMerchantDetailsServiceBuilder jdbc() {
		return new JdbcMerchantDetailsServiceBuilder();
	}

	public static final JdbcMerchantDetailsServiceBuilder jdbc(boolean cache) {
		return new JdbcMerchantDetailsServiceBuilder();
	}

	public static final JdbcMerchantDetailsServiceBuilder jdbc(DataSource source) {
		return new JdbcMerchantDetailsServiceBuilder(source);
	}

	public static final JdbcMerchantDetailsServiceBuilder jdbc(JdbcTemplate jdbcTemplate) {
		return new JdbcMerchantDetailsServiceBuilder(jdbcTemplate);
	}

	/**
	 * 构建对象并返回它或null。
	 *
	 * @return 如果实现允许，则要构建的对象或null。
	 */
	@Override
	public MerchantDetailsService build() {
		return performBuild();
	}

	/**
	 * 开始构建
	 *
	 * @return 商户列表服务
	 */
	protected MerchantDetailsService performBuild() {
		throw new UnsupportedOperationException("无法构建商家服务(需要使用inMemory()或jdbc())");
	}

	public void setConfigurer(PayMessageConfigurer configurer) {
		this.configurer = configurer;
	}
}
