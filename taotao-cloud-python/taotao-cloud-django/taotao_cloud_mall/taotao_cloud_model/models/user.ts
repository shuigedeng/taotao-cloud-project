import * as DataLoader from 'dataloader';

export default app => {
  const { ENUM, INTEGER, STRING } = app.Sequelize;

  // 用户
  const User = app.model.define('user', {
    // 用户的积分
    point: {
      type: INTEGER,
      allowNull: false,
      defaultValue: 0,
    },

    // 记录余额
    recordBalance: {
      type: INTEGER,
      allowNull: false,
      defaultValue: 0,
    },

    // 余额
    balance: {
      type: INTEGER,
      allowNull: false,
      defaultValue: 0,
    },

    // 会员到期日期
    memberExpiredDate: STRING(8),

    // 角色类型 管理员 财务 商户 商户管理员 广告管理员 商品管理员 会员 普通用户
    role: {
      type: ENUM,
      values: [
        'admin',
        'financial',
        'merchant',
        'keep',
        'advertising',
        'commodity',
        'member',
        'user',
      ],
      allowNull: false,
      defaultValue: 'user',
    },
  });

  // 关联模型
  User.associate = () => {
    const { WechatAccount, Phone, Card, TopUpOrder, Store, Balance, Item, Point, Account, Withdrawal, IssueCoupon } = app.model;

    // 微信信息
    User.hasOne(WechatAccount);

    // 号码
    User.hasOne(Phone);

    // 店铺管理员
    User.hasOne(Store);

    // 金额变动记录
    User.hasMany(Balance);

    User.hasMany(Card);

    // 金额变动记录
    User.hasMany(TopUpOrder);

    // 积分变动记录
    User.hasMany(Point);

    // 积分变动记录
    User.hasMany(Withdrawal);

    // 账户
    User.hasOne(Account);

    // 店铺创建者
    User.hasMany(Store, { as: 'addStore', foreignKey: 'founderId' });

    // 用户收藏的店铺
    User.belongsToMany(Item, { through: 'follow' });

    // 优惠券领取者
    User.belongsToMany(IssueCoupon, { through: 'draw' });

    // 邀请者
    User.belongsTo(User, { as: 'inviter', foreignKey: 'inviterId', targetKey: 'id' });

    // 受邀人
    User.hasMany(User, { as: 'invitees', foreignKey: 'inviterId', sourceKey: 'id' });
  };

  // 装载器
  const loader = new DataLoader(async primaryKeys => {
    const primaryKeyName = 'id';
    const items = await User.findAll({
      where: {
        [primaryKeyName]: {
          $in: primaryKeys,
        },
      },
      raw: true,
    });
    return primaryKeys.map(key =>
      items.find(currentValue => currentValue[primaryKeyName] === key),
    );
  });

  // 通过单主键清除缓存
  User.clearCacheByPrimaryKey = primaryKey => loader.clear(primaryKey);

  // 通过单主键获取
  User.fetchByPrimaryKey = primaryKey => loader.load(primaryKey);

  // 通过多主键获取
  User.fetchByPrimaryKeys = primaryKeys => loader.load(primaryKeys);

  // 通过单主键更新缓存
  User.updateCacheByPrimaryKey = (primaryKey, value) =>
    loader.clear(primaryKey).prime(primaryKey, value);

  return User;
};
