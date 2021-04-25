module.exports = app => {
    const { STRING, ENUM, INTEGER } = app.Sequelize;

    // 特价商品
    const OnSaleItem = app.model.define('on_sale_item', {
        // 价格
        price: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },

        // 单位
        unit: STRING,

        // 库存
        stock: {
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
    });

    OnSaleItem.associate = () => {
        // 关联模型
        const { ItemClassify, Project, OrderItem } = app.model;

        // 商品分类
        OnSaleItem.belongsTo(ItemClassify);

        // 商品专题
        OnSaleItem.belongsTo(Project);

        // 商品
        OnSaleItem.hasOne(OrderItem); 
    };

    return OnSaleItem;
};
