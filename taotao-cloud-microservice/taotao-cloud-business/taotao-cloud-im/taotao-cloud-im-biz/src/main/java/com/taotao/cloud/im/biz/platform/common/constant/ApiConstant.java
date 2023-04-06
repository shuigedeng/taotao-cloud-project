/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.im.biz.platform.common.constant;

/** 通用常量信息 */
public class ApiConstant {

    /** 登录用户 redis key */
    public static final String TOKEN_APP = "token:app:";

    /** 权限配置 Perm */
    public static final String PERM_APP = "app:app:app:app";

    /** 角色值 */
    public static final String ROLE_KEY = "app";

    /** 默认头像 */
    public static final String DEFAULT_PORTRAIT =
            "http://q3z3-im.oss-cn-beijing.aliyuncs.com/61bed1c563de173eb00e8d8c.png";

    /** 注销头像 */
    public static final String DELETED_PORTRAIT =
            "http://q3z3-im.oss-cn-beijing.aliyuncs.com/61bed1c563de173eb00e8d8c.png";

    /** 注销昵称 */
    public static final String DELETED_NICK_NAME = "已注销";

    /** base64图片前缀 */
    public static final String BASE64_PREFIX = "data:image/png;base64,";

    /** 用户二维码 */
    public static final String QR_CODE_USER = "user:";

    /** 群组二维码 */
    public static final String QR_CODE_GROUP = "group:";

    /** 二维码尺寸 */
    public static final Integer QR_CODE_SIZE = 600;

    /** 视频参数 */
    public static final String VIDEO_PARAM = "?x-oss-process=video/snapshot,t_1000,f_png,w_600,m_fast";

    /** 图片参数 */
    public static final String IMAGE_PARAM = "?x-oss-process=image/resize,w_200";

    /** redis_二维码 */
    public static final String REDIS_QR_CODE = "chat:qr_code:";

    /** redis_二维码时间(天) */
    public static final Integer REDIS_QR_CODE_TIME = 60;

    /** 新建群名称：好友创建通知 */
    public static final String GROUP_CREATE_NAME = "{}的群聊-{}";

    /** 不是你的好友 */
    public static final String FRIEND_NOT_EXIST = "对方不是你的好友";

    /** 通知：好友创建通知 */
    public static final String NOTICE_FRIEND_CREATE = "你们已经是好友啦，现在开始聊天吧";

    /** 通知：群组创建通知 */
    public static final String NOTICE_GROUP_CREATE_MASTER = "你邀请{}加入了群聊";

    /** 通知：群组创建通知 */
    public static final String NOTICE_GROUP_CREATE_MEMBER = "{}邀请你加入了群聊";

    /** 通知：群组退出通知 */
    public static final String NOTICE_GROUP_LOGOUT = "{}退出了群聊";

    /** 通知：群组转让通知 */
    public static final String NOTICE_GROUP_TRANSFER = "你已成为新群主";

    /** 通知：群组加入通知 */
    public static final String NOTICE_GROUP_JOIN = "{}加入了群聊";

    /** 通知：群组踢出通知 */
    public static final String NOTICE_GROUP_KICKED_MASTER = "{}被移出群聊";

    /** 通知：群组踢出通知 */
    public static final String NOTICE_GROUP_KICKED_MEMBER = "你被移出群聊";

    /** 通知：群组解散通知 */
    public static final String NOTICE_GROUP_DISSOLVE = "群主解散了群聊";

    /** 通知：群组改名 */
    public static final String NOTICE_GROUP_EDIT = "{}修改群名为“{}”";

    /** 通知：群组公告 */
    public static final String NOTICE_GROUP_NOTICE = "{}修改群公告为“{}”";

    /** 帖子_通知 */
    public static final String REDIS_TOPIC_NOTICE = "topic:notice:";

    /** 帖子_回复 */
    public static final String REDIS_TOPIC_REPLY = "topic:reply:";

    /** 好友_通知 */
    public static final String REDIS_FRIEND_NOTICE = "friend:notice:";

    /** redis_推送token */
    public static final String REDIS_PUSH_TOKEN = "chat:push:";

    /** redis_附近的人 */
    public static final String REDIS_NEAR = "chat:near";

    /** redis_摇一摇 */
    public static final String REDIS_SHAKE = "chat:shake";

    /** redis_摇一摇 */
    public static final String REDIS_GEO = "chat:geo";

    /** redis_消息_普通消息 */
    public static final String REDIS_MSG = "chat:msg:";

    /** redis_消息_大消息 */
    public static final String REDIS_MSG_BIG = "chat:msg_big:";

    /** redis_消息(天数) */
    public static final Integer REDIS_MSG_TIME = 60;

    /** redis_天气 */
    public static final String REDIS_MP_WEATHER = "mp:weather:{}:{}";

    /** redis_天气(分钟) */
    public static final Integer REDIS_MP_WEATHER_TIME = 30;

    /** redis_好友 */
    public static final String REDIS_FRIEND = "chat:friend:{}:{}";

    /** redis_好友(天) */
    public static final Integer REDIS_FRIEND_TIME = 60;

    /** redis_群组 */
    public static final String REDIS_GROUP_INFO = "chat:group:{}:{}";

    /** redis_群组(天) */
    public static final Integer REDIS_GROUP_TIME = 60;

    /** redis_实时音视频_签名 */
    public static final String REDIS_TRTC_SIGN = "chat:trtc:sign:";

    /** redis_实时音视频_用户前缀 */
    public static final String REDIS_TRTC_USER = "u";
}
