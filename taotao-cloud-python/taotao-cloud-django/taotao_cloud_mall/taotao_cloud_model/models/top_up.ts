module.exports = app => {
    const { INTEGER } = app.Sequelize;

    // 充值
    const TopUp = app.model.define('top_up', {

        // 价格
        price: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },

        // 赠送积分
        givePoint: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },

        // 排序
        order: INTEGER,
    });


    TopUp.associate = () => {
        const { TopUpRecord } = app.model;
        // 充值记录
        TopUp.hasMany(TopUpRecord);
        
    };

    return TopUp;
};
