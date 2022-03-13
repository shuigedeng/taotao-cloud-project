package com.taotao.cloud.message.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.message.biz.entity.ShortLink;
import com.taotao.cloud.message.biz.mapper.ShortLinkMapper;
import com.taotao.cloud.message.biz.service.ShortLinkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短链接 业务实现
 */
@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements
	ShortLinkService {

    @Override
    public List<ShortLink> queryShortLinks(ShortLink shortLink) {
        return this.list(PageUtil.initWrapper(shortLink));
    }
}
