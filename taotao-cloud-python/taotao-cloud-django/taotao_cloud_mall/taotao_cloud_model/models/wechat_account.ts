module.exports = app => {
  const { CHAR, STRING } = app.Sequelize;

  // 微信账户
  const WechatAccount = app.model.define('wechat_account', {
    // 开放ID
    openId: { type: CHAR(28), primaryKey: true },

    // 用户微信昵称
    nickname: STRING,

    // 用户头像
    avatarUrl: STRING,
  });

  WechatAccount.associate = () => {
    const { User } = app.model;
    // 用户
    WechatAccount.belongsTo(User);
  };

  return WechatAccount;
};
