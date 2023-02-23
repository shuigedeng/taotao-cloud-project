package com.taotao.cloud.workflow.biz.common.database.enums;


import com.taotao.cloud.workflow.biz.common.database.source.DbBase;

/**
 * 别名枚举
 *
 */
public enum DbAliasEnum {

    /**
     * 允空
     * 允许：1，不允许：0
     */
    ALLOW_NULL("F_ALLOW_NULL") {
        @Override
        public Integer isTrue() {
            return 1;
        }

        @Override
        public Integer isFalse() {
            return 0;
        }
    },
    /**
     * 主键
     * 存在：1，不存在：0
     */
    PRIMARY_KEY("F_PRIMARY_KEY") {
        @Override
        public Integer isTrue() {
            return 1;
        }

        @Override
        public Integer isFalse() {
            return 0;
        }
    },
    /**
     * 总数返回
     */
    TOTAL_RECORD("totalRecord");


    public Integer isTrue() {
        return null;
    }

    public Integer isFalse() {
        return null;
    }

    private final String alias;

    DbAliasEnum(String alias) {
        this.alias = alias;
    }

    public String AS() {
        return alias;
    }

    public String asByDb(DbBase db) {
        if (DbPostgre.class.equals(db.getClass())) {
            //postgre别名只能输出小写，Oracle只能大写
            //Mysql默认，SqlServer默认
            return alias.toLowerCase();
        } else if (DbOracle.class.equals(db.getClass())) {
            return alias.toUpperCase();
        } else {
            return alias;
        }
    }

    public static String getAsByDb(DbBase db, String keyWord) {
        if (DbPostgre.class.equals(db.getClass())) {
            //postgre别名只能输出小写，Oracle只能大写
            //Mysql默认，SqlServer默认
            return keyWord.toLowerCase();
        } else if (DbOracle.class.equals(db.getClass())) {
            return keyWord.toUpperCase();
        } else {
            return keyWord;
        }
    }

}
