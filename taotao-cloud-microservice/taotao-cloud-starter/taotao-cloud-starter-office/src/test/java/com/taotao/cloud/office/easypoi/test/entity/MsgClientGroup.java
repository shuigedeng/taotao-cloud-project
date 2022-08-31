package com.taotao.cloud.office.easypoi.test.entity;

import java.io.Serializable;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 客户分组表
 * 
 * @author yjl
 * 
 */
public class MsgClientGroup implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6946265640897464878L;

    // 组名
    @Excel(name = "分组")
    private String            groupName        = null;
    /**
     * 创建人
     */
    private String            createBy;

    // 一个组下 可能存在N多客户
    //private List<MsgClientMine> clients = null;

    /*@org.codehaus.jackson.annotate.JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.LAZY,mappedBy = "group")
    public List<MsgClientMine> getClients() {
    	return clients;
    }
    
    public void setClients(List<MsgClientMine> clients) {
    	this.clients = clients;
    }*/
    public String getCreateBy() {
        return createBy;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
