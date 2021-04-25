import * as DataLoader from 'dataloader';

export default app => {
  const { UUID, UUIDV1 } = app.Sequelize;

  // 文件
  const File = app.model.define('file', {
    // 主键
    key: {
      type: UUID,
      primaryKey: true,
      defaultValue: UUIDV1,
    },
  });

  // 装载器
  const loader = new DataLoader(async primaryKeys => {
    const primaryKeyName = 'key';
    const items = await File.findAll({
      where: {
        [primaryKeyName]: {
          $in: primaryKeys,
        },
      },
      raw: true,
    });
    return primaryKeys.map(key =>
      items.find(currentValue => currentValue[primaryKeyName] === key),
    );
  });

  // 通过单主键清除缓存
  File.clearCacheByPrimaryKey = primaryKey => loader.clear(primaryKey);

  // 通过单主键获取
  File.fetchByPrimaryKey = primaryKey => loader.load(primaryKey);

  // 通过多主键获取
  File.fetchByPrimaryKeys = primaryKeys => loader.load(primaryKeys);

  // 通过单主键更新缓存
  File.updateCacheByPrimaryKey = (primaryKey, value) =>
    loader.clear(primaryKey).prime(primaryKey, value);

  return File;
};
