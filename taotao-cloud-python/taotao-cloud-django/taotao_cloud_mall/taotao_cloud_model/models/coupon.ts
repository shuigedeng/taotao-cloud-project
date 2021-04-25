module.exports = app => {
  const { CHAR, DATE, INTEGER, ENUM } = app.Sequelize;

  // 优惠券
  const Coupon = app.model.define('coupon', {
    // 优惠券面额
    amount: { type: INTEGER, allowNull: false },
    // 要求（满减）
    require: { type: INTEGER, allowNull: false },
    // 使用时间
    usedAt: DATE,
    // 过期日期
    expiredDate: { type: CHAR(8), allowNull: false },
    // 类型 普通  会员
    type: {
      type: ENUM,
      values: ['ordinary', 'special'],
      allowNull: false,
      defaultValue: 'ordinary',
    },
  });

  Coupon.associate = () => {
    const { User, IssueCoupon, Order } = app.model;
    // 用户
    Coupon.belongsTo(User);

    // 优惠券发放
    Coupon.belongsTo(IssueCoupon);

    // 订单使用
    Coupon.belongsTo(Order);

  };

  return Coupon;
};
