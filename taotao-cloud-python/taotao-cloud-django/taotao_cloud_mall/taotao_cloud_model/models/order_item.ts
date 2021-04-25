module.exports = app => {
    const { INTEGER, STRING, ENUM, UUID } = app.Sequelize;

    // 订单商品
    const OrderItem = app.model.define('order_item', {
        // 哈希值
        fileKey: { type: UUID },
        // 商品标题
        title: { type: STRING, allowNull: false },
        // 单价
        price: { type: INTEGER, allowNull: false },
        // 下单价格
        amount: { type: INTEGER, allowNull: false },
        // 佣金
        commission: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },
        // 下单数量
        number: { type: INTEGER, allowNull: false },
        // 状态
        status: {
            type: ENUM,
            values: ['unpaid', 'completed'],
            allowNull: false,
            defaultValue: 'unpaid',
        },
    });

    OrderItem.associate = () => {
        const { Order, Item, OnSaleItem } = app.model;

        // 订单
        OrderItem.belongsTo(Order);

        // 商品
        OrderItem.belongsTo(Item);

        // 特价商品
        OrderItem.belongsTo(OnSaleItem);
    };

    return OrderItem;
};
