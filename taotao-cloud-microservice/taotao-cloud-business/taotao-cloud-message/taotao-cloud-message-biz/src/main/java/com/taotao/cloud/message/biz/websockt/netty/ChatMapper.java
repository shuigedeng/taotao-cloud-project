package com.taotao.cloud.message.biz.websockt.netty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {


}
