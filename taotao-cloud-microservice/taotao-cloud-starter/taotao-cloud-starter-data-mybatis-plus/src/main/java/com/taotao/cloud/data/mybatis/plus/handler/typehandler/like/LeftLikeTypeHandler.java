package com.taotao.cloud.data.mybatis.plus.handler.typehandler.like;


import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.taotao.cloud.data.mybatis.plus.handler.typehandler.like.BaseLikeTypeHandler;
import org.apache.ibatis.type.Alias;

/**
 * 仅仅用于like查询
 *
 * @author zuihou
 */
@Alias("leftLike")
public class LeftLikeTypeHandler extends BaseLikeTypeHandler {
    public LeftLikeTypeHandler() {
        super(SqlLike.LEFT);
    }
}
