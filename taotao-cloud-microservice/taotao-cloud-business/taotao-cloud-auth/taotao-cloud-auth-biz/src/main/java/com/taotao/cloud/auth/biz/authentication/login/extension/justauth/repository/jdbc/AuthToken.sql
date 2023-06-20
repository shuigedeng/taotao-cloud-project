-- AuthToken

CREATE TABLE `auth_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'tokenId',
  `enableRefresh` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否支持 refreshToken, 默认: 1. 1 表示支持, 0 表示不支持',
  `providerId` varchar(20) DEFAULT NULL COMMENT '第三方服务商,如: qq,github',
  `accessToken` varchar(512) DEFAULT NULL COMMENT 'accessToken',
  `expireIn` bigint(20) DEFAULT '-1' COMMENT 'accessToken 过期时间, 无过期时间默认为 -1',
  `refreshTokenExpireIn` bigint(20) DEFAULT '-1' COMMENT 'refreshToken 过期时间, 无过期时间默认为 -1',
  `refreshToken` varchar(512) DEFAULT NULL COMMENT 'refreshToken',
  `uid` varchar(20) DEFAULT NULL COMMENT 'alipay userId',
  `openId` varchar(256) DEFAULT NULL COMMENT 'qq/mi/toutiao/wechatMp/wechatOpen/weibo/jd/kujiale/dingTalk/douyin/feishu',
  `accessCode` varchar(256) DEFAULT NULL COMMENT 'dingTalk, taobao 附带属性',
  `unionId` varchar(256) DEFAULT NULL COMMENT 'QQ附带属性',
  `scope` varchar(256) DEFAULT NULL COMMENT 'Google附带属性',
  `tokenType` varchar(20) DEFAULT NULL COMMENT 'Google附带属性',
  `idToken` varchar(256) DEFAULT NULL COMMENT 'Google附带属性',
  `macAlgorithm` varchar(20) DEFAULT NULL COMMENT '小米附带属性',
  `macKey` varchar(256) DEFAULT NULL COMMENT '小米附带属性',
  `code` varchar(256) DEFAULT NULL COMMENT '企业微信附带属性',
  `oauthToken` varchar(256) DEFAULT NULL COMMENT 'Twitter附带属性',
  `oauthTokenSecret` varchar(256) DEFAULT NULL COMMENT 'Twitter附带属性',
  `userId` varchar(64) DEFAULT NULL COMMENT 'Twitter附带属性',
  `screenName` varchar(64) DEFAULT NULL COMMENT 'Twitter附带属性',
  `oauthCallbackConfirmed` varchar(64) DEFAULT NULL COMMENT 'Twitter附带属性',
  `expireTime` bigint(20) DEFAULT '-1' COMMENT '过期时间, 基于 1970-01-01T00:00:00Z, 无过期时间默认为 -1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4