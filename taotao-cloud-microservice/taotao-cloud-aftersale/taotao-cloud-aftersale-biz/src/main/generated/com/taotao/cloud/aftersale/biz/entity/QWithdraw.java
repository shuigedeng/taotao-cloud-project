package com.taotao.cloud.aftersale.biz.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWithdraw is a Querydsl query type for Withdraw
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWithdraw extends EntityPathBase<Withdraw> {

    private static final long serialVersionUID = -1490280939L;

    public static final QWithdraw withdraw = new QWithdraw("withdraw");

    public final com.taotao.cloud.data.jpa.entity.QBaseEntity _super = new com.taotao.cloud.data.jpa.entity.QBaseEntity(this);

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> balanceAmount = createNumber("balanceAmount", java.math.BigDecimal.class);

    public final StringPath code = createString("code");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    //inherited
    public final NumberPath<Long> createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    //inherited
    public final BooleanPath delFlag = _super.delFlag;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedTime = _super.lastModifiedTime;

    public final NumberPath<Long> mallId = createNumber("mallId", Long.class);

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QWithdraw(String variable) {
        super(Withdraw.class, forVariable(variable));
    }

    public QWithdraw(Path<? extends Withdraw> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWithdraw(PathMetadata metadata) {
        super(Withdraw.class, metadata);
    }

}

