module.exports = app => {
    const { INTEGER, STRING } = app.Sequelize;

    // 专题
    const Project = app.model.define('project', {
        // 名称
        title: { type: STRING, allowNull: false },
        // 图片
        imageKey: STRING,
        // 排序
        order: INTEGER,
    });
    Project.associate = () => {
        const { Item, OnSaleItem } = app.model;
        // 商品
        Project.hasMany(Item);

        // 特价商品
        Project.hasMany(OnSaleItem);
    };

    return Project;
};
