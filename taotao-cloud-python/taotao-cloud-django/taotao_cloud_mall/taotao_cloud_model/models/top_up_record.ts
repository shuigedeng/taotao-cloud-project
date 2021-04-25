'use strict';
module.exports = app => {
    const { BOOLEAN, INTEGER, STRING } = app.Sequelize;
    // 充值记录
    const TopUpRecord = app.model.define('Top_up_record', {
        // 变动后的余额
        balance: { type: INTEGER, allowNull: false },

        // 变动的金额
        price: { type: INTEGER, allowNull: false },

        // 增减与否
        add: { type: BOOLEAN, allowNull: false },

        // 赠送的积分
        point: { type: INTEGER, allowNull: false },

        // 备注
        remark: { type: STRING, allowNull: false },
    });

    TopUpRecord.associate = () => {
        const { User, TopUp } = app.model;
        // 用户
        TopUpRecord.belongsTo(User);
        // 充值
        TopUpRecord.belongsTo(TopUp);
    };

    return TopUpRecord;
};
