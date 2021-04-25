module.exports = app => {
    const { STRING, ENUM, INTEGER, CHAR } = app.Sequelize;

    // 商品
    const Item = app.model.define('item', {

        // 条形码
        code: {
            type: CHAR(13),
            allowNull: false,
            primaryKey: true,
        },

        // 主图
        imageKey: STRING,

        // 名称
        name: STRING,

        // 简介
        content: STRING,

        // 积分最大抵扣金额
        pointDiscountPrice: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },

        // 原价
        originalPrice: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },

        // 价格
        price: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },

        // 会员价格
        memberPrice: {
            type: INTEGER,
            allowNull: false,
            defaultValue: 0,
        },

        // 佣金
        commission: {
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

        // 种类  普通   专享
        kind: {
            type: ENUM,
            values: ['ordinary', 'special'],
            allowNull: false,
            defaultValue: 'ordinary',
        },

        // 类型（决定商品是否可以退款）
        type: {
            type: ENUM,
            values: ['ordinary', 'special'],
            allowNull: false,
            defaultValue: 'ordinary',
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

    Item.associate = () => {
        // 关联模型
        const { ItemClassify, ItemImage, OrderItem, Project, User } = app.model;

        // 商品分类
        Item.belongsTo(ItemClassify);

        // 专题
        Item.belongsTo(Project);

        // 商品图片
        Item.hasMany(ItemImage);

        // 商品图片
        Item.hasMany(OrderItem);

        // 被收藏的商品
        Item.belongsToMany(User, { through: 'follow' });
    };

    return Item;
};
