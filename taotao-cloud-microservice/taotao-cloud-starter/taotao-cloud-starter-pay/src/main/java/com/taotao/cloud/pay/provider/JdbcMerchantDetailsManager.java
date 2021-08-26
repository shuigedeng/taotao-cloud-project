package com.taotao.cloud.pay.provider;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.util.str.StringUtils;
import com.taotao.cloud.pay.configurers.PayMessageConfigurer;
import com.taotao.cloud.pay.merchant.bean.CommonPaymentPlatformMerchantDetails;
import com.taotao.cloud.pay.utils.SqlTools;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static com.egzosn.pay.spring.boot.core.utils.SqlTools.SEPARATED;


/**
 * JDBC支付账户(商户)存储器
 *
 * @author egan
 *         <pre>
 *                 email egzosn@gmail.com
 *                 date 2018-11-22 17:18:03
 *                 </pre>
 */
public class JdbcMerchantDetailsManager implements MerchantDetailsManager<CommonPaymentPlatformMerchantDetails> {


    private static final String TABLE = "merchant_details";
    private static final List<String> FIELDS = Arrays.asList("appid", "pay_type", "mch_id", "cert_store_type", "key_private", "key_cert_pwd", "key_public", "key_cert", "notify_url", "return_url", "sign_type", "seller", "sub_app_id", "sub_mch_id", "input_charset", "is_test");
    private static final String SELECT_FIELDS = SqlTools.join(FIELDS, SEPARATED);
    private static final String ID = "details_id";

    private static final String DEFAULT_BASE_QUERY_SQL = SqlTools.getSelectSQL(ID + SEPARATED + SELECT_FIELDS, TABLE);
    private static final String DEFAULT_FIND_BY_ID_SQL = DEFAULT_BASE_QUERY_SQL + " where " + ID + "=?";
    private static final String DEFAULT_INSERT_SQL = "insert into " + TABLE + " (" + ID + SEPARATED + SELECT_FIELDS + ") values (" + SqlTools.forQuestionMarkSQL(FIELDS.size() + 1) + ")";
    private static final String DEFAULT_UPDATE_SQL = SqlTools.generateUpdateByRowIdString(TABLE, FIELDS, ID).toString();
    private static final String DEFAULT_DELETE_SQL = "delete from " + TABLE + " where " + ID + "=?";
    public static final String DEFAULT_EXISTS_SQL = "select " + ID + " from " + TABLE + " where " + ID + " = ?";

    private JdbcTemplate jdbcTemplate;

    private String findByIdSql = DEFAULT_FIND_BY_ID_SQL;
    private String insertSql = DEFAULT_INSERT_SQL;
    private String updateSql = DEFAULT_UPDATE_SQL;
    private String deleteSql = DEFAULT_DELETE_SQL;
    private String existsSql = DEFAULT_EXISTS_SQL;

    private PayMessageConfigurer configurer;

    public JdbcMerchantDetailsManager(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcMerchantDetailsManager(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * 创建商户
     *
     * @param merchants 商户信息
     */
    @Override
    public void createMerchant(Collection<CommonPaymentPlatformMerchantDetails> merchants) {
        for (CommonPaymentPlatformMerchantDetails merchant : merchants) {
            this.createMerchant(merchant);
        }
    }
    /**
     * 创建商户
     *
     * @param merchant 商户信息
     */
    @Override
    public void createMerchant(CommonPaymentPlatformMerchantDetails merchant) {

        Assert.isTrue(!merchantExists(merchant.getDetailsId()), "商户信息已存在");
        Object[] args = new Object[]{merchant.getDetailsId(), merchant.getAppId(), merchant.getPayType(), merchant.getMchId(), merchant.getCertStoreType(), merchant.getKeyPrivate(), merchant.getKeyPrivateCertPwd(), merchant.getKeyPublic(), merchant.getKeyCert(), merchant.getNotifyUrl(), merchant.getReturnUrl(), merchant.getSignType(), merchant.getSeller(), merchant.getSubAppId(), merchant.getSubMchId(), merchant.getInputCharset(), merchant.isTest()};
        jdbcTemplate.update(insertSql, args);


    }

    /**
     * 更新商户
     *
     * @param merchant 商户信息
     */
    @Override
    public void updateMerchant(CommonPaymentPlatformMerchantDetails merchant) {
        Assert.isTrue(merchantExists(merchant.getDetailsId()), "商户信息不存在");
        Object[] args = new Object[]{merchant.getAppId(), merchant.getPayType(), merchant.getMchId(), merchant.getCertStoreType(), merchant.getKeyPrivate(), merchant.getKeyPrivateCertPwd(), merchant.getKeyPublic(), merchant.getKeyCert(), merchant.getNotifyUrl(), merchant.getReturnUrl(), merchant.getSignType(), merchant.getSeller(), merchant.getSubAppId(), merchant.getSubMchId(), merchant.getInputCharset(), merchant.isTest(), merchant.getDetailsId()};
        jdbcTemplate.update(updateSql, args);
    }

    /**
     * 删除商户
     *
     * @param id 商户id
     */
    @Override
    public void deleteMerchant(String id) {
        jdbcTemplate.update(deleteSql, id);
    }

    /**
     * 检查商户是否存在
     *
     * @param merchantId 商户id
     * @return 检查商户是否存在
     */
    @Override
    public boolean merchantExists(String merchantId) {
        List<String> ids = jdbcTemplate.queryForList(existsSql, String.class, merchantId);
        if (ids.size() > 0) {
            throw new IncorrectResultSizeDataAccessException("出现重复的支付商户id", 1);
        }

        return !ids.isEmpty();
    }

    /**
     * 通过支付商户id加载对应的商户信息列表
     *
     * @param merchantId 支付商户id
     * @return 商户信息列表
     */
    @Override
    public CommonPaymentPlatformMerchantDetails loadMerchantByMerchantId(String merchantId) {
        List<CommonPaymentPlatformMerchantDetails> detailss = jdbcTemplate.query(findByIdSql, (ResultSet rs, int i)->{
                CommonPaymentPlatformMerchantDetails details = new CommonPaymentPlatformMerchantDetails();
                details.setDetailsId(rs.getString(1));
                details.setAppId(rs.getString(2));
                details.setPayType(rs.getString(3));
                details.setMchId(rs.getString(4));
                String certStoreType = rs.getString(5);
                if (StringUtils.isNotEmpty(certStoreType)) {
                    details.setCertStoreType(CertStoreType.valueOf(certStoreType));
                }
                if (details.getCertStoreType() == CertStoreType.INPUT_STREAM) {
                    setKeyPrivate(details, rs);
                    details.setKeyCert(rs.getAsciiStream(9));
                }
                else {
                    details.setKeystore(rs.getString(6));
                    details.setKeyPublicCert(rs.getString(8));
                    details.setKeyCert(rs.getString(9));
                }
                details.setKeystorePwd(rs.getString(7));
                details.setNotifyUrl(rs.getString(10));
                details.setReturnUrl(rs.getString(11));
                details.setSignType(rs.getString(12));
                details.setSeller(rs.getString(13));
                details.setSubAppId(rs.getString(14));
                details.setSubMchId(rs.getString(15));
                details.setInputCharset(rs.getString(16));
                details.setTest(rs.getBoolean(17));
                PayService payService = details.initService().getPayService();
                InMemoryMerchantDetailsManager.setPayMessageConfigurer(payService, details, configurer);
                return details;
        }, merchantId);
        int size = detailss != null ? detailss.size() : 0;
        if (size == 0) {
            throw new MerchantNotFoundException(merchantId);
        }
        else if (size > 1) {
            throw new IncorrectResultSizeDataAccessException(1, size);
        }
        else {
            return detailss.get(0);
        }
    }


    public void setKeyPrivate(CommonPaymentPlatformMerchantDetails details, ResultSet rs) throws SQLException {
        if (UnionPaymentPlatform.platformName.equals(details.getPayType())) {
            details.setKeystore(rs.getAsciiStream(6));
            details.setKeyPublicCert(rs.getAsciiStream(8));
        }
        else {
            details.setKeyPrivate(rs.getString(6));
            details.setKeyPublic(rs.getString(8));
        }

    }

    public String getFindByIdSql() {
        return findByIdSql;
    }

    public void setFindByIdSql(String findByIdSql) {
        this.findByIdSql = findByIdSql;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    public String getDeleteSql() {
        return deleteSql;
    }

    public void setDeleteSql(String deleteSql) {
        this.deleteSql = deleteSql;
    }

    public String getExistsSql() {
        return existsSql;
    }

    public void setExistsSql(String existsSql) {
        this.existsSql = existsSql;
    }


    /**
     * 设置支付消息配置中心
     *
     * @param configurer 配置
     */
    @Override
    public void setPayMessageConfigurer(PayMessageConfigurer configurer) {
        this.configurer = configurer;
    }



}
