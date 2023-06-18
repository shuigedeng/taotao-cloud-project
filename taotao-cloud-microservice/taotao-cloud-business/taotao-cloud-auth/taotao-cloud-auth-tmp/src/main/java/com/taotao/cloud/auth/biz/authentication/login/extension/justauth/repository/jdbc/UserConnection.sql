-- This SQL contains a "create table" that can be used to create a table that Auth2JdbcUsersConnectionRepository can persist
-- connection in. It is, however, not to be assumed to be production-ready, all-purpose SQL. It is merely representative
-- of the kind of table that Auth2JdbcUsersConnectionRepository works with. The table and column names, as well as the
-- general
-- column types, are what is important. Specific column types and sizes that work may vary across database vendors and
-- the required sizes may vary across API providers.

CREATE TABLE `user_connection` (
   `userId` varchar(36) NOT NULL COMMENT '本地用户id',
   `providerId` varchar(20) NOT NULL COMMENT '第三方服务商',
   `providerUserId` varchar(36) NOT NULL COMMENT '第三方用户id',
   `rank` int(11) NOT NULL COMMENT 'userId 绑定同一个 providerId 的排序',
   `displayName` varchar(64) DEFAULT NULL COMMENT '第三方用户名',
   `profileUrl` varchar(256) DEFAULT NULL COMMENT '主页',
   `imageUrl` varchar(256) DEFAULT NULL COMMENT '头像',
   `accessToken` varchar(512) NOT NULL,
   `tokenId` bigint(20) DEFAULT NULL COMMENT 'auth_token.id',
   `refreshToken` varchar(512) DEFAULT NULL,
   `expireTime` bigint(20) DEFAULT '-1' COMMENT '过期时间, 基于 1970-01-01T00:00:00Z, 无过期时间默认为 -1',
   PRIMARY KEY (`userId`,`providerId`,`providerUserId`),
   UNIQUE KEY `idx_userId_providerId_rank` (`userId`,`providerId`,`rank`),
   KEY `idx_providerId_providerUserId_rank` (`providerId`,`providerUserId`,`rank`),
   KEY `idx_tokenId` (`tokenId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4