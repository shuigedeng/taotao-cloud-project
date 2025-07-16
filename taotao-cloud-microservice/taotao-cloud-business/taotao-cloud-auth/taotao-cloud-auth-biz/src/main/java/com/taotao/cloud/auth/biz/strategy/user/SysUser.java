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

package com.taotao.cloud.auth.biz.strategy.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.MoreObjects;
import com.taotao.boot.common.enums.DataItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

/**
 * <p>系统用户 </p>
 *
 */
@Schema(title = "系统用户")
@Entity
@Table(
        name = "sys_user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_name"})},
        indexes = {
            @Index(name = "sys_user_id_idx", columnList = "user_id"),
            @Index(name = "sys_user_unm_idx", columnList = "user_name")
        })
@Cacheable
// @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region =
// UpmsConstants.REGION_SYS_USER)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class SysUser {

    @Schema(title = "用户ID")
    @Id
    @UuidGenerator
    @Column(name = "user_id", length = 64)
    private String userId;

    @Schema(title = "用户名")
    @Column(name = "user_name", length = 128, unique = true)
    private String userName;

    @Schema(title = "密码", description = "BCryptPasswordEncoder")
    @Column(name = "password", length = 256)
    private String password;

    @Schema(title = "昵称")
    @Column(name = "nick_name", length = 64)
    private String nickName;

    @Schema(title = "手机号码")
    @Column(name = "phone_number", length = 256)
    private String phoneNumber;

    @Schema(title = "头像")
    @Column(name = "avatar", length = 1024)
    private String avatar;

    @Schema(title = "EMAIL")
    @Column(name = "email", length = 100)
    private String email;

    @Schema(title = "账户过期日期")
    @Column(name = "account_expire_at")
    private LocalDateTime accountExpireAt;

    @Schema(title = "密码过期日期")
    @Column(name = "credentials_expire_at")
    private LocalDateTime credentialsExpireAt;

    @Schema(name = "数据状态")
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private DataItemStatus status = DataItemStatus.ENABLE;

    @Schema(name = "是否为保留数据", description = "True 为不能删，False为可以删除")
    @Column(name = "is_reserved")
    private Boolean reserved = Boolean.FALSE;

    @Schema(name = "版本号")
    @Column(name = "reversion")
    private Integer reversion = 0;

    /**
     * 角色描述,UI界面显示使用
     */
    @Schema(name = "备注")
    @Column(name = "description", length = 512)
    private String description;

    //	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region =
    // UpmsConstants.REGION_SYS_ROLE)
    @Schema(title = "用户角色")
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})},
            indexes = {
                @Index(name = "sys_user_role_uid_idx", columnList = "user_id"),
                @Index(name = "sys_user_role_rid_idx", columnList = "role_id")
            })
    private Set<SysRole> roles = new HashSet<>();

    /**
     * SysUser 和 SysEmploye 为双向一对一关系，SysUser 为维护端。
     * <p>
     * 存在一个问题，SysUser新增操作没有问题，修改操作就会抛出
     * InvalidDataAccessApiUsageException: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing : cn.ttc.cloud.upms.logic.entity.system.SysUser.employee -> cn.ttc.cloud.upms.logic.entity.hr.SysEmployee; nested exception is java.lang.IllegalStateException: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing
     * <p>
     * 最初以为是 JPA @OneToOne 相关配置不对或者是 JPA save()方法的问题，以及 Hibernate 对象在Session是否是游离状态等几方面问题，尝试解决的好久都没有解决。
     * <p>
     * 后面发现，@RequestBody 会将参数绑定为实体。
     * 在新增操作时，只传递了几个参数，没有employee属性，所以SysUser的employee属性的反序列化之后值是null。
     * 在修改操作时，employee的是 '{}', Jackson在反序列化的过程中就将其转换为完整的对象结构，但是相关的值是空。
     * <p>
     * 这就导致，JPA 在存储数据时，employee 是游离状态的对象，所以一直抛错。
     * <p>
     * 这也就解释了下面问题：
     * 当前 @OneToOne 配置，与上面 @ManyToMany 配置基本一致，而且都是使用 @JoinTable 的方式。
     * 那么同样的更新操作@OneToOne会出错，@ManyToMany就不会出错？
     * <p>
     * 因为 @ManyToMany 使用的是集合，空对象不会被转成对象Set进去。
     */
    //	@JsonDeserialize(using = SysEmployeeEmptyToNull.class)
    //	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region =
    // UpmsConstants.REGION_SYS_EMPLOYEE)
    @Schema(title = "人员")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_user_employee",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "employee_id"})},
            indexes = {
                @Index(name = "sys_user_employee_sid_idx", columnList = "user_id"),
                @Index(name = "sys_user_employee_eid_idx", columnList = "employee_id")
            })
    private SysEmployee employee;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Set<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }

    public SysEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(SysEmployee employee) {
        this.employee = employee;
    }

    public LocalDateTime getAccountExpireAt() {
        return accountExpireAt;
    }

    public void setAccountExpireAt(LocalDateTime accountExpireAt) {
        this.accountExpireAt = accountExpireAt;
    }

    public LocalDateTime getCredentialsExpireAt() {
        return credentialsExpireAt;
    }

    public void setCredentialsExpireAt(LocalDateTime credentialsExpireAt) {
        this.credentialsExpireAt = credentialsExpireAt;
    }

    public DataItemStatus getStatus() {
        return status;
    }

    public void setStatus(DataItemStatus status) {
        this.status = status;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public Integer getReversion() {
        return reversion;
    }

    public void setReversion(Integer reversion) {
        this.reversion = reversion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("userName", userName)
                .add("password", password)
                .add("nickName", nickName)
                .add("phoneNumber", phoneNumber)
                .add("avatar", avatar)
                .add("email", email)
                .toString();
    }
}
