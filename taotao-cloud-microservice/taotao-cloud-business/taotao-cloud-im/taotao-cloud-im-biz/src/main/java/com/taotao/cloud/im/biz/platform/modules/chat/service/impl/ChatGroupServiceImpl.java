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

package com.taotao.cloud.im.biz.platform.modules.chat.service.impl;

import com.github.pagehelper.PageHelper;
import com.platform.common.constant.ApiConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupDao;
import com.platform.modules.chat.domain.ChatApply;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.domain.ChatGroupInfo;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.ApplyStatusEnum;
import com.platform.modules.chat.enums.ApplyTypeEnum;
import com.platform.modules.chat.service.ChatApplyService;
import com.platform.modules.chat.service.ChatGroupInfoService;
import com.platform.modules.chat.service.ChatGroupService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.GroupVo02;
import com.platform.modules.chat.vo.GroupVo03;
import com.platform.modules.chat.vo.GroupVo07;
import com.platform.modules.chat.vo.GroupVo08;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.service.ChatPushService;
import com.platform.modules.push.vo.PushParamVo;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/** 群组 服务层实现 q3z3 */
@Service("chatGroupService")
public class ChatGroupServiceImpl extends BaseServiceImpl<ChatGroup> implements ChatGroupService {

    @Resource
    private ChatGroupDao chatGroupDao;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatGroupInfoService groupInfoService;

    @Resource
    private ChatApplyService chatApplyService;

    @Resource
    private ChatPushService chatPushService;

    @Autowired
    private RedisUtils redisUtils;

    private static final Integer GROUP_COUNT = 9;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupDao);
    }

    @Override
    public List<ChatGroup> queryList(ChatGroup t) {
        List<ChatGroup> dataList = chatGroupDao.queryList(t);
        return dataList;
    }

    @Transactional
    @Override
    public void createGroup(List<Long> list) {
        Long userId = ShiroUtils.getUserId();
        // 传入用户
        List<ChatUser> userList = verifyUserList(list);
        // 当前登录人
        ChatUser master = chatUserService.getById(userId);
        // 当前时间
        Date now = DateUtil.date();
        // 建群
        ChatGroup group = new ChatGroup()
                .setMaster(master.getUserId())
                .setName(
                        StrUtil.format(ApiConstant.GROUP_CREATE_NAME, master.getNickName(), RandomUtil.randomString(4)))
                .setPortrait(initPortrait(userList, master))
                .setCreateTime(now);
        this.add(group);
        // 群明细
        List<ChatGroupInfo> infoList = new ArrayList<>();
        for (ChatUser user : userList) {
            infoList.add(new ChatGroupInfo(user.getUserId(), group.getId()).setKeepGroup(YesOrNoEnum.YES));
        }
        infoList.add(new ChatGroupInfo(master.getUserId(), group.getId()).setKeepGroup(YesOrNoEnum.YES));
        groupInfoService.batchAdd(infoList);
        String groupName = formatGroupName(group.getId(), group.getName());
        PushParamVo paramVo1 = new PushParamVo()
                .setUserId(group.getId())
                .setNickName(groupName)
                .setPortrait(group.getPortrait())
                .setContent(StrUtil.format(ApiConstant.NOTICE_GROUP_CREATE_MEMBER, master.getNickName()));
        // 通知组员
        chatPushService.pushMsg(formatFrom(list, paramVo1), paramVo1, PushMsgTypeEnum.ALERT);
        // 通知群主
        List<String> nickList = userList.stream().map(ChatUser::getNickName).toList();
        String content = StrUtil.format(ApiConstant.NOTICE_GROUP_CREATE_MASTER, CollUtil.join(nickList, "、"));
        PushParamVo paramVo2 = new PushParamVo()
                .setUserId(group.getId())
                .setNickName(groupName)
                .setPortrait(group.getPortrait())
                .setContent(content);
        chatPushService.pushMsg(paramVo2.setToId(userId), PushMsgTypeEnum.ALERT);
    }

    /** 计算群名 */
    private String formatGroupName(Long groupId, String groupName) {
        if (StringUtils.isEmpty(groupName)) {
            groupName = getById(groupId).getName();
        }
        Long count = groupInfoService.countByGroup(groupId);
        return StrUtil.format("{}({})", groupName, count);
    }

    @Override
    public Dict getInfo(Long groupId) {
        // 校验
        ChatGroup group = this.getById(groupId);
        if (group == null) {
            throw new BaseException("当前群不存在");
        }
        Long userId = ShiroUtils.getUserId();
        ChatGroupInfo info = groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.YES);
        // 组装返回值
        Dict groupDict = Dict.create().parseBean(group).filter("name", "notice").set("groupId", group.getId());
        Dict setDict = Dict.create().parseBean(info).filter("top", "disturb", "keepGroup");
        List<Long> userList = groupInfoService.queryUserList(groupId);
        List<Dict> userDict = chatUserService.getByIds(userList).stream()
                .collect(
                        ArrayList::new,
                        (x, y) -> {
                            x.add(Dict.create().parseBean(y).filter("userId", "nickName", "portrait"));
                        },
                        ArrayList::addAll);
        return Dict.create()
                .set("user", userDict)
                .set("group", groupDict)
                .set("set", setDict)
                .set("master", group.getMaster().equals(userId) ? YesOrNoEnum.YES : YesOrNoEnum.NO);
    }

    @Transactional
    @Override
    public void invitationGroup(Long groupId, List<Long> list) {
        // 查询用户列表
        List<ChatUser> userList = verifyUserList(list);
        // 当前用户
        Long userId = ShiroUtils.getUserId();
        // 验证群
        groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.YES);
        // 查询数量
        Long count = groupInfoService.countByGroup(groupId);
        // 集合
        Map<Long, ChatGroupInfo> infoMap = groupInfoService.queryUserMap(groupId);
        // 群明细
        List<ChatGroupInfo> infoList = new ArrayList<>();
        List<ChatUser> newUserList = new ArrayList<>();
        for (ChatUser chatUser : userList) {
            ChatGroupInfo groupInfo = infoMap.get(chatUser.getUserId());
            // 新增
            if (groupInfo == null) {
                infoList.add(new ChatGroupInfo(chatUser.getUserId(), groupId));
                newUserList.add(chatUser);
            }
            // 更新
            else if (YesOrNoEnum.YES.equals(groupInfo.getKicked())) {
                groupInfoService.updateById(
                        new ChatGroupInfo().setInfoId(groupInfo.getInfoId()).setKicked(YesOrNoEnum.NO));
                newUserList.add(chatUser);
            }
        }
        // 批量信息
        groupInfoService.batchAdd(infoList);
        if (count <= GROUP_COUNT) {
            updPortrait(groupId);
        }
        // 通知
        ChatGroup group = getById(groupId);
        List<String> nickList = newUserList.stream().map(ChatUser::getNickName).toList();
        String content = StrUtil.format(ApiConstant.NOTICE_GROUP_JOIN, CollUtil.join(nickList, "、"));
        List<PushParamVo> pushParamList = queryPushParam(group, content);
        chatPushService.pushMsg(pushParamList, PushMsgTypeEnum.ALERT);
    }

    /** 更新群头像 */
    private void updPortrait(Long groupId) {
        // 执行
        ThreadUtil.execAsync(() -> {
            // 执行分页
            PageHelper.startPage(1, GROUP_COUNT, "info_id");
            // 查询所有成员
            List<Long> userList = groupInfoService.queryUserList(groupId);
            String portrait = initPortrait(chatUserService.getByIds(userList), null);
            this.updateById(new ChatGroup().setId(groupId).setPortrait(portrait));
        });
    }

    /** 更新群主 */
    @Transactional
    protected void updMaster(ChatGroup group) {
        Long groupId = group.getId();
        // 执行分页
        PageHelper.startPage(1, 1, "info_id");
        // 查询所有成员
        List<Long> userList = groupInfoService.queryUserList(groupId);
        if (userList.size() == 0) {
            // 删除群
            this.deleteById(groupId);
            return;
        }
        Long userId = userList.get(0);
        // 更新群主
        this.updateById(new ChatGroup().setId(groupId).setMaster(userId));
        groupInfoService.updateById(new ChatGroupInfo().setInfoId(userId).setKeepGroup(YesOrNoEnum.YES));
        // 更新申请人
        ChatApply query = new ChatApply()
                .setTargetId(groupId)
                .setApplyType(ApplyTypeEnum.GROUP)
                .setApplyStatus(ApplyStatusEnum.NONE);
        chatApplyService.queryList(query).forEach(e -> {
            chatApplyService.updateById(new ChatApply().setId(e.getId()).setToId(userId));
        });
        ChatGroupInfo groupInfo = groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.NO);
        String groupName = formatGroupName(group.getId(), group.getName());
        // 通知新群主
        PushParamVo paramVo = new PushParamVo()
                .setUserId(group.getId())
                .setNickName(groupName)
                .setPortrait(group.getPortrait())
                .setDisturb(groupInfo.getDisturb())
                .setTop(groupInfo.getTop())
                .setContent(ApiConstant.NOTICE_GROUP_TRANSFER);
        chatPushService.pushMsg(paramVo.setToId(userId), PushMsgTypeEnum.ALERT);
    }

    /** 验证人员信息 */
    private List<ChatUser> verifyUserList(List<Long> list) {
        // 验证
        if (CollectionUtils.isEmpty(list)) {
            throw new BaseException("好友列表不能为空");
        }
        if (list.contains(ShiroUtils.getUserId())) {
            throw new BaseException("好友列表不能包含自己");
        }
        // 去重
        list = list.stream().distinct().toList();
        // 验证
        if (CollectionUtils.isEmpty(list)) {
            throw new BaseException("好友列表不能为空");
        }
        // 查询用户列表
        return chatUserService.getByIds(list);
    }

    @Transactional
    @Override
    public void kickedGroup(Long groupId, List<Long> list) {
        // 查询用户列表
        List<ChatUser> userList = verifyUserList(list);
        // 当前用户
        Long userId = ShiroUtils.getUserId();
        // 验证群
        ChatGroupInfo groupInfo = groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.YES);
        // 查询群
        ChatGroup group = getById(groupId);
        if (!group.getMaster().equals(userId)) {
            throw new BaseException("你不是群主，不能操作");
        }
        // 删除成员
        groupInfoService.queryUserList(groupId, list).forEach(e -> {
            groupInfoService.updateById(
                    new ChatGroupInfo().setInfoId(e.getInfoId()).setKicked(YesOrNoEnum.YES));
        });
        // 更新头像
        updPortrait(groupId);
        String groupName = formatGroupName(group.getId(), group.getName());
        // 群主
        List<String> nickList = userList.stream().map(ChatUser::getNickName).toList();
        PushParamVo paramVo1 = new PushParamVo()
                .setUserId(group.getId())
                .setNickName(groupName)
                .setPortrait(group.getPortrait())
                .setDisturb(groupInfo.getDisturb())
                .setTop(groupInfo.getTop())
                .setContent(StrUtil.format(ApiConstant.NOTICE_GROUP_KICKED_MASTER, CollUtil.join(nickList, "、")));
        chatPushService.pushMsg(paramVo1.setToId(group.getMaster()), PushMsgTypeEnum.ALERT);
        // 组员
        List<PushParamVo> paramList = queryGroupPushFrom(groupId, list, ApiConstant.NOTICE_GROUP_KICKED_MEMBER);
        chatPushService.pushMsg(paramList, PushMsgTypeEnum.ALERT);
        // 删除缓存
        groupInfoService.delGroupInfoCache(groupId, list);
    }

    @Override
    public String getGroupQrCode(Long groupId) {
        String key = ApiConstant.REDIS_QR_CODE + groupId;
        if (redisUtils.hasKey(key)) {
            return redisUtils.get(key);
        }
        ChatGroup group = this.getById(groupId);
        if (group == null) {
            throw new BaseException("当前群不存在");
        }
        String content = ApiConstant.QR_CODE_GROUP + groupId;
        byte[] data = QrCodeUtil.generatePng(content, ApiConstant.QR_CODE_SIZE, ApiConstant.QR_CODE_SIZE);
        String value = ApiConstant.BASE64_PREFIX.concat(Base64.encode(data));
        redisUtils.set(key, value, ApiConstant.REDIS_QR_CODE_TIME, TimeUnit.DAYS);
        return value;
    }

    @Override
    public void editTop(Long groupId, YesOrNoEnum top) {
        Long userId = ShiroUtils.getUserId();
        ChatGroupInfo info = groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.YES);
        groupInfoService.updateById(
                new ChatGroupInfo().setInfoId(info.getInfoId()).setTop(top));
        // 删除缓存
        groupInfoService.delGroupInfoCache(groupId, Arrays.asList(userId));
    }

    @Override
    public void editDisturb(Long groupId, YesOrNoEnum disturb) {
        Long userId = ShiroUtils.getUserId();
        ChatGroupInfo info = groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.YES);
        groupInfoService.updateById(
                new ChatGroupInfo().setInfoId(info.getInfoId()).setDisturb(disturb));
        // 删除缓存
        groupInfoService.delGroupInfoCache(groupId, Arrays.asList(userId));
    }

    @Override
    public void editKeepGroup(Long groupId, YesOrNoEnum keepGroup) {
        Long userId = ShiroUtils.getUserId();
        ChatGroupInfo info = groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.YES);
        groupInfoService.updateById(
                new ChatGroupInfo().setInfoId(info.getInfoId()).setKeepGroup(keepGroup));
        // 删除缓存
        groupInfoService.delGroupInfoCache(groupId, Arrays.asList(userId));
    }

    @Transactional
    @Override
    public void logoutGroup(Long groupId) {
        Long userId = ShiroUtils.getUserId();
        ChatUser chatUser = chatUserService.getById(userId);
        ChatGroupInfo groupInfo = groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.YES);
        ChatGroup group = getById(groupId);
        // 删除数据
        groupInfoService.deleteById(groupInfo.getInfoId());
        // 删除缓存
        groupInfoService.delGroupInfoCache(groupId, Arrays.asList(userId));
        if (group.getMaster().equals(userId)) {
            // 变更群主
            ThreadUtil.execAsync(() -> {
                updMaster(group);
            });
        } else {
            String groupName = formatGroupName(groupId, group.getName());
            // 通知
            PushParamVo paramVo = new PushParamVo()
                    .setUserId(group.getId())
                    .setPortrait(group.getPortrait())
                    .setNickName(groupName)
                    .setDisturb(groupInfo.getDisturb())
                    .setTop(groupInfo.getTop())
                    .setContent(StrUtil.format(ApiConstant.NOTICE_GROUP_LOGOUT, chatUser.getNickName()))
                    .setToId(group.getMaster());
            chatPushService.pushMsg(paramVo, PushMsgTypeEnum.ALERT);
        }
        // 更新头像
        updPortrait(groupId);
    }

    @Transactional
    @Override
    public void removeGroup(Long groupId) {
        Long userId = ShiroUtils.getUserId();
        ChatGroup group = getById(groupId);
        if (group == null) {
            throw new BaseException("当前群组不存在");
        }
        if (!group.getMaster().equals(userId)) {
            throw new BaseException("你不是群主，不能操作");
        }
        List<PushParamVo> userList = queryPushParam(group, ApiConstant.NOTICE_GROUP_DISSOLVE);
        this.deleteById(groupId);
        // 删除数据
        groupInfoService.delByGroup(groupId);
        // 通知
        chatPushService.pushMsg(userList, PushMsgTypeEnum.ALERT);
    }

    private List<PushParamVo> queryPushParam(ChatGroup group, String content) {
        List<ChatGroupInfo> dataList = groupInfoService.queryList(
                new ChatGroupInfo().setGroupId(group.getId()).setKicked(YesOrNoEnum.NO));
        String groupName = formatGroupName(group.getId(), group.getName());
        List<PushParamVo> arrayList = new ArrayList<>();
        dataList.forEach(e -> {
            PushParamVo paramVo = new PushParamVo()
                    .setUserId(group.getId())
                    .setNickName(groupName)
                    .setPortrait(group.getPortrait())
                    .setToId(e.getUserId())
                    .setDisturb(e.getDisturb())
                    .setTop(e.getTop())
                    .setContent(content);
            arrayList.add(paramVo);
        });
        return arrayList;
    }

    @Override
    public GroupVo07 scanCode(String param) {
        // 校验前缀
        if (!StrUtil.startWith(param, ApiConstant.QR_CODE_GROUP)) {
            throw new BaseException("参数错误");
        }
        Long groupId = Convert.toLong(ReUtil.get(PatternPool.NUMBERS, param, 0), null);
        ChatGroup group = getById(groupId);
        if (group == null) {
            throw new BaseException("当前群组不存在");
        }
        Long count = groupInfoService.countByGroup(groupId);
        return BeanUtil.toBean(group, GroupVo07.class)
                .setPortrait(JSONUtil.toList(group.getPortrait(), String.class))
                .setCount(count);
    }

    @Override
    public void joinGroup(Long groupId) {
        Long userId = ShiroUtils.getUserId();
        ChatGroupInfo info = groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.NO);
        ChatGroup group = this.getById(groupId);
        // 加群
        if (info == null) {
            groupInfoService.add(new ChatGroupInfo(userId, groupId));
            // 全员
            // 查询当前登录f
            ChatUser chatUser = chatUserService.getById(userId);
            String content = StrUtil.format(ApiConstant.NOTICE_GROUP_JOIN, chatUser.getNickName());
            List<PushParamVo> userList = queryPushParam(group, content);
            // 通知
            chatPushService.pushMsg(userList, PushMsgTypeEnum.ALERT);
        }
        // 申请
        else if (YesOrNoEnum.YES.equals(info.getKicked())) {
            chatApplyService.applyGroup(group.getMaster());
        }
    }

    @Override
    public List<GroupVo08> groupList() {
        // 结果
        List<GroupVo08> dataList = new ArrayList<>();
        // 查询明细
        List<Long> groupList =
                groupInfoService
                        .queryList(new ChatGroupInfo()
                                .setUserId(ShiroUtils.getUserId())
                                .setKicked(YesOrNoEnum.NO))
                        .stream()
                        .map(ChatGroupInfo::getGroupId)
                        .toList();
        // 集合判空
        if (CollectionUtils.isEmpty(groupList)) {
            return dataList;
        }
        // 查询群组
        this.getByIds(groupList).forEach(e -> {
            GroupVo08 groupVo = BeanUtil.toBean(e, GroupVo08.class)
                    .setPortrait(JSONUtil.toList(e.getPortrait(), String.class))
                    .setGroupId(e.getId());
            dataList.add(groupVo);
        });
        return dataList;
    }

    @Override
    public List<PushParamVo> queryFriendPushFrom(Long groupId, String content) {
        Long userId = ShiroUtils.getUserId();
        List<PushParamVo> paramList = chatGroupDao.queryFriendPushFrom(groupId, userId);
        ChatUser fromUser = chatUserService.getById(userId);
        paramList.forEach(e -> {
            e.setUserId(fromUser.getUserId());
            if (StringUtils.isEmpty(e.getNickName())) {
                e.setNickName(fromUser.getNickName());
            }
            e.setPortrait(fromUser.getPortrait());
            e.setContent(content);
        });
        return paramList;
    }

    @Override
    public List<PushParamVo> queryGroupPushFrom(Long groupId, List<Long> list, String content) {
        // 查询群
        ChatGroup group = getById(groupId);
        String groupName = formatGroupName(groupId, group.getName());
        // 成员
        List<ChatGroupInfo> groupInfoList = groupInfoService.queryUserList(groupId, list);
        List<PushParamVo> paramList = new ArrayList<>();
        groupInfoList.forEach(e -> {
            PushParamVo paramVo = new PushParamVo()
                    .setUserId(group.getId())
                    .setNickName(groupName)
                    .setPortrait(group.getPortrait())
                    .setToId(e.getUserId())
                    .setDisturb(e.getDisturb())
                    .setTop(e.getTop())
                    .setContent(ApiConstant.NOTICE_GROUP_KICKED_MEMBER);
            paramList.add(paramVo);
        });
        return paramList;
    }

    @Override
    public void editGroupName(GroupVo02 groupVo) {
        Long userId = ShiroUtils.getUserId();
        Long groupId = groupVo.getGroupId();
        ChatUser chatUser = chatUserService.getById(userId);
        groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.YES);
        ChatGroup group = new ChatGroup().setId(groupId).setName(groupVo.getName());
        updateById(group);
        String groupName = formatGroupName(groupId, group.getName());
        PushParamVo paramVo = new PushParamVo()
                .setUserId(group.getId())
                .setNickName(groupName)
                .setPortrait(group.getPortrait())
                .setContent(StrUtil.format(ApiConstant.NOTICE_GROUP_EDIT, chatUser.getNickName(), group.getName()));
        // 通知组员
        List<Long> userList = groupInfoService.queryUserList(groupId);
        chatPushService.pushMsg(formatFrom(userList, paramVo), paramVo, PushMsgTypeEnum.ALERT);
    }

    @Override
    public void editGroupNotice(GroupVo03 groupVo) {
        Long userId = ShiroUtils.getUserId();
        Long groupId = groupVo.getGroupId();
        groupInfoService.getGroupInfo(groupId, userId, YesOrNoEnum.YES);
        ChatGroup group = new ChatGroup().setId(groupVo.getGroupId()).setNotice(groupVo.getNotice());
        updateById(group);
        String groupName = formatGroupName(groupId, null);
        ChatUser chatUser = chatUserService.getById(userId);
        PushParamVo paramVo = new PushParamVo()
                .setUserId(groupId)
                .setNickName(groupName)
                .setPortrait(getById(groupId).getPortrait())
                .setContent(StrUtil.format(ApiConstant.NOTICE_GROUP_NOTICE, chatUser.getNickName(), group.getNotice()));
        // 通知组员
        List<Long> userList = groupInfoService.queryUserList(groupId);
        chatPushService.pushMsg(formatFrom(userList, paramVo), paramVo, PushMsgTypeEnum.ALERT);
    }

    private List<PushParamVo> formatFrom(List<Long> userList, PushParamVo paramVo) {
        List<PushParamVo> paramList = new ArrayList<>();
        userList.forEach(e -> {
            paramList.add(paramVo.setToId(e));
        });
        return paramList;
    }

    /** 格式化头像 */
    private String initPortrait(List<ChatUser> userList, ChatUser master) {
        List<ChatUser> newUserList = userList;
        if (master != null) {
            newUserList = CollUtil.newArrayList(master);
            newUserList.addAll(userList);
        }
        return JSONUtil.toJsonStr(CollUtil.sub(newUserList, 0, GROUP_COUNT).stream()
                .map(ChatUser::getPortrait)
                .toList());
    }
}
