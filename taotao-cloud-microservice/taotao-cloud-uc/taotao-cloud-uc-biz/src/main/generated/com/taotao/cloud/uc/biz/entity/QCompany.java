package com.taotao.cloud.uc.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = -1988712621L;

    public static final QCompany company = new QCompany("company");

    public final com.taotao.cloud.data.jpa.entity.QBaseEntity _super = new com.taotao.cloud.data.jpa.entity.QBaseEntity(this);

    public final StringPath address = createString("address");

    //inherited
    public final NumberPath<Long> createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final StringPath creditCode = createString("creditCode");

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    public final StringPath domain = createString("domain");

    public final StringPath email = createString("email");

    public final StringPath fullName = createString("fullName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final StringPath regionInfo = createString("regionInfo");

    public final StringPath tenantId = createString("tenantId");

    public final StringPath tenantSecret = createString("tenantSecret");

    public final NumberPath<Byte> type = createNumber("type", Byte.class);

    public final StringPath username = createString("username");

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public final StringPath webSite = createString("webSite");

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    public QCompany(Path<? extends Company> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompany(PathMetadata metadata) {
        super(Company.class, metadata);
    }

}

