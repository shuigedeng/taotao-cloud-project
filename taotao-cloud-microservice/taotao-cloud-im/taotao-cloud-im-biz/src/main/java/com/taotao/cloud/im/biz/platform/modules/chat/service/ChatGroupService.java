package com.taotao.cloud.im.biz.platform.modules.chat.service;

import cn.hutool.core.lang.Dict;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.vo.GroupVo02;
import com.platform.modules.chat.vo.GroupVo03;
import com.platform.modules.chat.vo.GroupVo07;
import com.platform.modules.chat.vo.GroupVo08;
import com.platform.modules.push.vo.PushParamVo;

import java.util.List;

/**
 * <p>
 * 群组 服务层
 * q3z3
 * </p>
 */
public interface ChatGroupService extends BaseService<ChatGroup> {

    /**
     * 建群
     */
    void createGroup(List<Long> list);

    /**
     * 群详情
     */
    Dict getInfo(Long groupId);

    /**
     * 邀请群组
     */
    void invitationGroup(Long groupId, List<Long> list);

    /**
     * 踢出群组
     */
    void kickedGroup(Long groupId, List<Long> list);

    /**
     * 获取群二维码
     */
    String getGroupQrCode(Long groupId);

    /**
     * 修改置顶
     */
    void editTop(Long groupId, YesOrNoEnum top);

    /**
     * 修改免打扰
     */
    void editDisturb(Long groupId, YesOrNoEnum disturb);

    /**
     * 修改保存群组
     */
    void editKeepGroup(Long groupId, YesOrNoEnum keepGroup);

    /**
     * 退出群组
     */
    void logoutGroup(Long groupId);

    /**
     * 解散群组
     */
    void removeGroup(Long groupId);

    /**
     * 扫码查询
     */
    GroupVo07 scanCode(String param);

    /**
     * 加入群组
     */
    void joinGroup(Long groupId);

    /**
     * 查询群列表
     */
    List<GroupVo08> groupList();

    /**
     * 查询用户id
     */
    List<PushParamVo> queryFriendPushFrom(Long groupId, String content);

    /**
     * 查询用户id
     */
    List<PushParamVo> queryGroupPushFrom(Long groupId, List<Long> list, String content);

    /**
     * 修改群名
     */
    void editGroupName(GroupVo02 groupVo);

    /**
     * 修改群公告
     */
    void editGroupNotice(GroupVo03 groupVo);
}
