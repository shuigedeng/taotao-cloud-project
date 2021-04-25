module.exports = app => {
  const { ENUM, INTEGER, STRING } = app.Sequelize;
  const Withdrawal = app.model.define('withdrawal', {
    // 姓名
    name: STRING,
    // 提现人电话
    phone: STRING(11),
    // 提现金额
    price: { type: INTEGER, allowNull: false },
    // 提现状态
    status: {
      type: ENUM,
      values: ['success', 'loading', 'audit', 'refuse'],
      defaultValue: 'loading',
      allowNull: false,
    },
    // 类型
    type: {
      type: ENUM,
      values: ['user', 'store'],
      allowNull: false,
    },
    // 账户
    account: STRING,
    // 卡号
    card: STRING,
    // 备注
    remark: STRING,
    // 排序
    order: INTEGER,
  });
  Withdrawal.associate = () => {
    // 用户
    Withdrawal.belongsTo(app.model.User);
    // 店铺
    Withdrawal.belongsTo(app.model.Store);
  };

  return Withdrawal;
};
