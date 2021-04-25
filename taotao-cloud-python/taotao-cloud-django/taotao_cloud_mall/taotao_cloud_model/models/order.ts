module.exports = app => {
    const { INTEGER, ENUM, CHAR, STRING } = app.Sequelize;

    // 订单
    const Order = app.model.define('order', {
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
        // 价格
        price: { type: INTEGER, allowNull: false },
        // 积分抵扣金额
        pointDiscount: { type: INTEGER, allowNull: false, defaultValue: 0 },
        // 优惠金额
        discount: { type: INTEGER, allowNull: false, defaultValue: 0 },
        // 运费
        freight: { type: INTEGER, allowNull: false, defaultValue: 0 },
        // 总计
        amount: { type: INTEGER, allowNull: false },
        // 取货码
        code: CHAR(8),
        // 配送时间
        time: STRING,

        // 类型 配送类型 自提类型 店铺直购
        type: {
            type: ENUM,
            values: ['distribution', 'unmanned', 'storeBuy'],
            allowNull: false,
            defaultValue: 'unmanned',
        },
        // 支付方式
        payment: {
            type: ENUM,
            values: ['wechatPay', 'balance'],
            allowNull: false,
            defaultValue: 'wechatPay',
        },
        // 订单状态
        status: {
            type: ENUM,
            values: ['unpaid', 'paid', 'fetch', 'completed'],
            allowNull: false,
            defaultValue: 'unpaid',
        },
    }, {    // 软删除
        paranoid: true,
    });

    Order.associate = () => {
        const { User, DistributionUser, Trade, Store, OrderItem, Coupon, Balance, Point, Address } = app.model;
        // 用户
        Order.belongsTo(User);

        // 交易单号
        Order.hasOne(Trade);

        // 金额变动
        Order.hasOne(Balance);

        // 金额变动
        Order.hasOne(Address);

        // 优惠券
        Order.hasOne(Coupon);

        // 积分记录
        Order.hasOne(Point);

        // 积分记录
        Order.hasOne(DistributionUser);

        // 店铺
        Order.belongsTo(Store);

        // 订单内的商品
        Order.hasMany(OrderItem);

    }


    return Order;
};
