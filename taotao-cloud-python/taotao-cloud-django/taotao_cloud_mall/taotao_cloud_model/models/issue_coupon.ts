module.exports = app => {
  const { DATE, INTEGER, ENUM } = app.Sequelize;

  // 优惠券
  const IssueCoupon = app.model.define('issue_coupon', {
    // 优惠券面额
    amount: { type: INTEGER, allowNull: false },

    // 要求（满减）
    require: { type: INTEGER, allowNull: false },

    // 过期日期
    expiredDate: DATE,

    // 数量
    number: {
      type: INTEGER,
      allowNull: false,
      defaultValue: 0,
    },

    // 类型 普通  会员
    type: {
      type: ENUM,
      values: ['ordinary', 'special'],
      allowNull: false,
      defaultValue: 'ordinary',
    },

    // 状态
    status: {
      type: ENUM,
      values: ['draft', 'published'],
      allowNull: false,
      defaultValue: 'draft',
    },
    // 排序
    order: INTEGER,
  });

  IssueCoupon.associate = () => {
    const { Coupon, User } = app.model;
    // 优惠券
    IssueCoupon.hasMany(Coupon);
    // 被收藏的店铺
    IssueCoupon.belongsToMany(User, { through: 'draw' });
  };

  return IssueCoupon;
};
