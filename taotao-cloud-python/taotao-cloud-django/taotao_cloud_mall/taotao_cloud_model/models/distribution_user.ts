'use strict';
module.exports = app => {
    const { STRING } = app.Sequelize;
    // 金额变动记录
    const DistributionUser = app.model.define('distribution_user', {
        name: { type: STRING, allowNull: false },
        phone: { type: STRING, allowNull: false },
    });

    DistributionUser.associate = () => {
        // 订单
        DistributionUser.belongsTo(app.model.Order);
    };

    return DistributionUser;
};
