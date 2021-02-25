package com.taotao.cloud.uc.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysDictItem is a Querydsl query type for SysDictItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysDictItem extends EntityPathBase<SysDictItem> {

    private static final long serialVersionUID = 548816300L;

    public static final QSysDictItem sysDictItem = new QSysDictItem("sysDictItem");

    public final com.taotao.cloud.data.jpa.entity.QBaseEntity _super = new com.taotao.cloud.data.jpa.entity.QBaseEntity(this);

    //inherited
    public final NumberPath<Long> createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final StringPath description = createString("description");

    public final NumberPath<Long> dictId = createNumber("dictId", Long.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath itemText = createString("itemText");

    public final StringPath itemValue = createString("itemValue");

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final BooleanPath status = createBoolean("status");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QSysDictItem(String variable) {
        super(SysDictItem.class, forVariable(variable));
    }

    public QSysDictItem(Path<? extends SysDictItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysDictItem(PathMetadata metadata) {
        super(SysDictItem.class, metadata);
    }

}

