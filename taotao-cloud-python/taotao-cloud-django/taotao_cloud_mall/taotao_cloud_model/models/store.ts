module.exports = app => {
    const { DECIMAL, STRING, ENUM, INTEGER } = app.Sequelize;

    // 门店
    const Store = app.model.define('store', {

        // 主图
        imageKey: STRING,

        // 名称
        name: { type: STRING },

        // 地址
        address: { type: STRING },

        // 经度
        longitude: DECIMAL(10, 6),

        // 纬度
        latitude: DECIMAL(10, 6),

        // 总销售额
        sales: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },

        // 类型  普通   专供
        type: {
            type: ENUM,
            values: ['ordinary', 'special'],
            allowNull: false,
            defaultValue: 'ordinary',
        },

        // 销售额
        balance: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
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
    }, {    // 软删除
        paranoid: true,
    });

    Store.associate = () => {
        // 关联模型
        const { Order, Trade, User, Balance, Withdrawal } = app.model;
        // 店铺管理员
        Store.belongsTo(User);

        // 订单号
        Store.hasMany(Order);

        // 交易单号
        Store.hasMany(Trade);

        // 提现记录
        Store.hasMany(Withdrawal);

        // 金额记录
        Store.hasMany(Balance);

        // 创建者
        Store.belongsTo(User, { as: 'founder' });

    };

    return Store;
};
