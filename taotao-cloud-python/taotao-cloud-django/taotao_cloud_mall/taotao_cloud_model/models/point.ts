module.exports = app => {
  const { BOOLEAN, INTEGER, STRING } = app.Sequelize;
  // 积分变动记录
  const Point = app.model.define('point', {
    point: { type: INTEGER, allowNull: false }, // 余额
    price: { type: INTEGER, allowNull: false }, // 金额
    add: { type: BOOLEAN, allowNull: false }, // 增减与否
    remark: { type: STRING, allowNull: false }, // 备注
  });

  Point.associate = () => {

    const { TopUpOrder, User, Order } = app.model;
    // 用户
    Point.belongsTo(User);
    // 订单
    Point.belongsTo(Order);
    // 订单
    Point.belongsTo(TopUpOrder);
  };

  return Point;
};
