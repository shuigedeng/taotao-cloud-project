### 数据来源(数据格式)

```
  前端采集数据
    -- 小程序端日志数据 miniLog.json
    -- h5和web端日志数据 webLog.json
    -- ios端日志数据 iosLog.json
    -- android端日志数据 androidLog.json

  后端采集数据
    -- 系统日志数据 feignSysLogRequest.json
    -- 请求日志数据 requestLog.json
    -- 业务日志数据 businessLog.json

  爬虫端采集数据
    -- 待实现

  元数据
    -- 系统元数据 meta.json
```

### 数仓分层

```
数据引入层 ODS

数据公共层 CDM

   公共维度层 DIM
   
   公共粒度汇总事实层 DWS
   
   明细粒度事实层 DWD
   
数据表现层 ADS
```

### 数仓开发规范

```
1. 数据库命名
    数仓对应分层_(业务线|业务项目)  
    ods_taotao_cloud_mall (原始数据)
    dwd_taotao_cloud_mall (主题/事实数据)
    dws_taotao_cloud_mall (主题/事实汇总数据 基于主题宽表汇总数据)
    dim_taotao_cloud_mall (维度数据)
    ads_taotao_cloud_mall (应用统计指标数据)
    mid_taotao_cloud_mall (中间数据)
    tmp_taotao_cloud_mall (临时数据)
    
2. 表命名
    ods_taotao_cloud_mall_web_log
    ods_taotao_cloud_mall_mini_log
    ods_taotao_cloud_mall_ios_log
    ods_taotao_cloud_mall_android_log
    ods_taotao_cloud_mall_sys_log
    ods_taotao_cloud_mall_rqeust_log
    ods_taotao_cloud_mall_business_user_login
    ods_taotao_cloud_mall_business_user_update
    ods_taotao_cloud_mall_business_user_logout
    ods_taotao_cloud_mall_business_proudct_add
    ods_taotao_cloud_mall_business_proudct_update
    ods_taotao_cloud_mall_business_order_add
    ods_taotao_cloud_mall_business_order_update
    ods_taotao_cloud_mall_business_order_pay
    ods_taotao_cloud_mall_business_order_refund_pay

    mid_taotao_cloud_mall_web_log_parquet
    mid_taotao_cloud_mall_mini_log_parquet
    mid_taotao_cloud_mall_ios_log_parquet
    mid_taotao_cloud_mall_android_log_parquet
    mid_taotao_cloud_mall_sys_log_parquet
    mid_taotao_cloud_mall_rqeust_log_parquet
    mid_taotao_cloud_mall_business_user_login_parquet
    mid_taotao_cloud_mall_business_user_update_parquet
    mid_taotao_cloud_mall_business_user_logout_parquet
    mid_taotao_cloud_mall_business_proudct_add_parquet
    mid_taotao_cloud_mall_business_proudct_update_parquet
    mid_taotao_cloud_mall_business_order_add_parquet
    mid_taotao_cloud_mall_business_order_update_parquet
    mid_taotao_cloud_mall_business_order_pay_parquet
    mid_taotao_cloud_mall_business_order_refund_pay_parquet

    dwd_taotao_cloud_mall_user_logproview (xx用户基于产品浏览日志事实表)
    dwd_taotao_cloud_mall_user_comment (xx用户关注事实表)

    dws_taotao_cloud_mall_order_nd (xx用户订单n天统计表)
    dws_taotao_cloud_mall_cmtpro_nd (xx用户产品关注汇总n天统计表)

    dim_taotao_cloud_mall_pub_date (时间维度表)
    dim_taotao_cloud_mall_pub_area (地区维度表)
    dim_taotao_cloud_mall_pub_category (商品分类维度表)
    dim_taotao_cloud_mall_pub_dict (公共字典维度表)

    ads_taotao_cloud_mall_order_from (订单统计表)
    ads_taotao_cloud_mall_order_pay_from (订单支付统计表)
```
