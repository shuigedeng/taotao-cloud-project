package com.taotao.cloud.data.mybatis.plus.handler.objecthandler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.taotao.cloud.common.constant.StrPool;
import com.taotao.cloud.common.utils.common.IdGeneratorUtils;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.data.mybatis.plus.base.entity.MpSuperEntity;
import com.taotao.cloud.data.mybatis.plus.properties.MybatisPlusAutoFillProperties;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * 自定义填充公共字段
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/5/2 11:22
 */
public class AutoFieldMetaObjectHandler implements MetaObjectHandler {

	private final MybatisPlusAutoFillProperties autoFillProperties;

	public AutoFieldMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties) {
		this.autoFillProperties = autoFillProperties;
	}

	/**
	 * 是否开启了插入填充
	 */
	@Override
	public boolean openInsertFill() {
		return autoFillProperties.getEnableInsertFill();
	}

	/**
	 * 是否开启了更新填充
	 */
	@Override
	public boolean openUpdateFill() {
		return autoFillProperties.getEnableUpdateFill();
	}

	/**
	 * 插入填充，字段为空自动填充
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		fillId(metaObject);

		Object createTime = getFieldValByName(autoFillProperties.getCreateTimeField(),
			metaObject);
		Object updateTime = getFieldValByName(autoFillProperties.getUpdateTimeField(),
			metaObject);
		if (createTime == null || updateTime == null) {
			if (createTime == null) {
				fillCreated(metaObject);
			}
			if (updateTime == null) {
				fillUpdated(metaObject);
			}
		}
	}

	private void fillId(MetaObject metaObject) {
		Long id = IdGeneratorUtils.getId();

		//1. 继承了SuperEntity 若 ID 中有值，就不设置
		if (metaObject.getOriginalObject() instanceof MpSuperEntity) {
			Object oldId = ((MpSuperEntity) metaObject.getOriginalObject()).getId();
			if (oldId != null) {
				return;
			}

			Object idVal = StrPool.STRING_TYPE_NAME.equals(
				metaObject.getGetterType(autoFillProperties.getIdField()).getName())
				? String.valueOf(id)
				: id;
			this.setFieldValByName(autoFillProperties.getIdField(), idVal, metaObject);
			return;
		}

		// 2. 没有继承SuperEntity， 但主键的字段名为：id
		if (metaObject.hasGetter(autoFillProperties.getIdField())) {
			Object oldId = metaObject.getValue(autoFillProperties.getIdField());
			if (oldId != null) {
				return;
			}

			Object idVal = StrPool.STRING_TYPE_NAME.equals(
				metaObject.getGetterType(autoFillProperties.getIdField()).getName())
				? String.valueOf(id)
				: id;
			this.setFieldValByName(autoFillProperties.getIdField(), idVal, metaObject);
			return;
		}

		// 3. 实体没有继承 Entity 和 SuperEntity，且 主键名为其他字段
		TableInfo tableInfo = TableInfoHelper.getTableInfo(
			metaObject.getOriginalObject().getClass());
		if (tableInfo == null) {
			return;
		}

		// 主键类型
		Class<?> keyType = tableInfo.getKeyType();
		if (keyType == null) {
			return;
		}

		// id 字段名
		String keyProperty = tableInfo.getKeyProperty();
		Object oldId = metaObject.getValue(keyProperty);
		if (oldId != null) {
			return;
		}

		// 反射得到 主键的值
		Field idField = ReflectUtil.getField(metaObject.getOriginalObject().getClass(), keyProperty);
		Object fieldValue = ReflectUtil.getFieldValue(metaObject.getOriginalObject(), idField);
		// 判断ID 是否有值，有值就不
		if (ObjectUtil.isNotEmpty(fieldValue)) {
			return;
		}

		Object idVal =
			keyType.getName().equalsIgnoreCase(StrPool.STRING_TYPE_NAME) ? String.valueOf(id) : id;
		this.setFieldValByName(keyProperty, idVal, metaObject);
	}

	private void fillCreated(MetaObject metaObject) {
		// 设置创建时间和创建人
		if (metaObject.getOriginalObject() instanceof MpSuperEntity) {
			created(metaObject);
			return;
		}

		if (metaObject.hasGetter(autoFillProperties.getCreateByField())) {
			Object oldVal = metaObject.getValue(autoFillProperties.getCreateByField());
			if (oldVal == null) {
				this.setFieldValByName(autoFillProperties.getCreateByField(), SecurityUtils.getUserIdWithAnonymous(),
					metaObject);
			}
		}

		if (metaObject.hasGetter(autoFillProperties.getCreateTimeField())) {
			Object oldVal = metaObject.getValue(autoFillProperties.getCreateTimeField());
			if (oldVal == null) {
				this.setFieldValByName(autoFillProperties.getCreateTimeField(),
					LocalDateTime.now(), metaObject);
			}
		}
	}

	private void created(MetaObject metaObject) {
		MpSuperEntity entity = (MpSuperEntity) metaObject.getOriginalObject();
		if (entity.getCreateTime() == null) {
			this.setFieldValByName(autoFillProperties.getCreateTimeField(), LocalDateTime.now(),
				metaObject);
		}

		if (entity.getCreatedBy() == null || entity.getCreatedBy().equals(0)) {
			Object userIdVal = SecurityUtils.getUserIdWithAnonymous();
			this.setFieldValByName(MpSuperEntity.CREATED_BY, userIdVal, metaObject);
		}
	}

	private void fillUpdated(MetaObject metaObject) {
		// 修改人 修改时间
		if (metaObject.getOriginalObject() instanceof MpSuperEntity) {
			update(metaObject);
			return;
		}

		if (metaObject.hasGetter(autoFillProperties.getUpdateByField())) {
			Object oldVal = metaObject.getValue(autoFillProperties.getUpdateByField());
			if (oldVal == null) {
				this.setFieldValByName(autoFillProperties.getUpdateByField(), SecurityUtils.getUserIdWithAnonymous(),
					metaObject);
			}
		}

		if (metaObject.hasGetter(autoFillProperties.getUpdateTimeField())) {
			Object oldVal = metaObject.getValue(autoFillProperties.getUpdateTimeField());
			if (oldVal == null) {
				this.setFieldValByName(autoFillProperties.getUpdateTimeField(),
					LocalDateTime.now(), metaObject);
			}
		}
	}

	private void update(MetaObject metaObject) {
		MpSuperEntity entity = (MpSuperEntity) metaObject.getOriginalObject();
		if (entity.getUpdatedBy() == null || entity.getUpdatedBy().equals(0)) {
			Object userIdVal = SecurityUtils.getUserIdWithAnonymous();
			this.setFieldValByName(MpSuperEntity.UPDATED_BY, userIdVal, metaObject);
		}

		if (entity.getUpdateTime() == null) {
			this.setFieldValByName(autoFillProperties.getUpdateTimeField(), LocalDateTime.now(),
				metaObject);
		}
	}

	/**
	 * 更新填充
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		fillUpdated(metaObject);
	}
}

