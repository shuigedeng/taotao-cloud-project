module.exports = app => {
    const { INTEGER, ENUM, CHAR } = app.Sequelize;

    // 订单
    const TopUpOrder = app.model.define('top_up_order', {
        // ID
        id: {
            type: CHAR(32),
            primaryKey: true,
            defaultValue: () => { // 生成32位订单号
                const totalLength = 32;
                let string = String(new Date().getTime());
                for (let i = 0; i < totalLength - string.length; i++) {
                    const num = Math.floor(Math.random() * 10);
                    string += num;
                }
                return string;
            },
        },
        // 总计
        amount: { type: INTEGER, allowNull: false },

        // 赠送积分
        givePoint: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },
        // 状态
        status: {
            type: ENUM,
            values: ['unpaid', 'completed'],
            allowNull: false,
            defaultValue: 'unpaid',
        },
    });

    TopUpOrder.associate = () => {
        const { User, TopUpTrade, Balance, Point } = app.model;
        // 用户
        TopUpOrder.belongsTo(User);
        // 交易单号
        TopUpOrder.hasOne(TopUpTrade);
        // 余额变动记录
        TopUpOrder.hasOne(Balance);
        // 积分变动记录
        TopUpOrder.hasOne(Point);

    }


    return TopUpOrder;
};
