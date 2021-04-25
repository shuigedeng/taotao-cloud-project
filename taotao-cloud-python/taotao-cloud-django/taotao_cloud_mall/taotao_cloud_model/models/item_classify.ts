module.exports = app => {
  const { INTEGER, STRING } = app.Sequelize;

  // 商品分类
  const ItemClassify = app.model.define('item_classify', {

    // 名称
    title: { type: STRING, allowNull: false },

    // 图片
    imageKey: STRING,

    // 排序
    order: INTEGER,
  }, {    // 软删除
      paranoid: true,
    });

  ItemClassify.associate = () => {
    const { Item, OnSaleItem } = app.model;

    // 商品
    ItemClassify.hasMany(Item);

    // 特价商品
    ItemClassify.hasMany(OnSaleItem);

  }
  return ItemClassify;
};
