package com.taotao.cloud.core.heaven.util.id.impl;

/**
 * Id 工具類
 */
@Deprecated
public final class Ids {

    private Ids(){}

    /**
     * uuid32
     * @return string
     * @since 0.1.37
     */
    public static String uuid32() {
        return UUID32.getInstance().genId();
    }

}
