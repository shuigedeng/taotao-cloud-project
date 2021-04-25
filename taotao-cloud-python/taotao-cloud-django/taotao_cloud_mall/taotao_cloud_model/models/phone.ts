module.exports = app => {
  const { CHAR } = app.Sequelize;

  // 号码
  const Phone = app.model.define('phone', {
    // 手机号
    number: { type: CHAR(11), primaryKey: true },
  });

  Phone.associate = () => {
    const { User } = app.model;
    // 用户
    Phone.belongsTo(User);
  };

  return Phone;
};
