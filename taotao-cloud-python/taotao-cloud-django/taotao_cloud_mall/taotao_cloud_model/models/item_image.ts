module.exports = app => {
    const { INTEGER, STRING } = app.Sequelize;

    // 商品图片
    const ItemImage = app.model.define('item_image', {

        // 图片
        imageKey: STRING,

        // 排序
        order: INTEGER,
    });

    ItemImage.associate = () => {
        // 关联模型
        const { Item } = app.model;

        // 商品
        ItemImage.belongsTo(Item);
    };

    return ItemImage;
};
