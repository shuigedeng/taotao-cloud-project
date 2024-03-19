package com.taotao.cloud.realtime.mall.app.func;

import com.taotao.cloud.realtime.mall.common.GmallConstant;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

/**
 *
 * Date: 2021/3/1
 * Desc:
 */
@FunctionHint(output = @DataTypeHint("ROW<ct BIGINT,source STRING>"))
public class KeywordProductC2RUDTF extends TableFunction<Row> {
    public void eval(Long clickCt, Long cartCt, Long orderCt) {

        if(clickCt>0L) {
            Row rowClick = new Row(2);
            rowClick.setField(0, clickCt);
            rowClick.setField(1, GmallConstant.KEYWORD_CLICK);
            collect(rowClick);
        }
        if(cartCt>0L) {
            Row rowCart = new Row(2);
            rowCart.setField(0, cartCt);
            rowCart.setField(1, GmallConstant.KEYWORD_CART);
            collect(rowCart);
        }
        if(orderCt>0) {
            Row rowOrder = new Row(2);
            rowOrder.setField(0, orderCt);
            rowOrder.setField(1, GmallConstant.KEYWORD_ORDER);
            collect(rowOrder);
        }

    }
}

