package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.dao;

import com.qf.ems.entity.Emp;
import com.qf.ems.entity.Page;

import java.util.List;

public interface EmpDao {
    public List<Emp> selectAll(Page page);
    public long selectCount();
    public int delete(int id);
    public int insert(Emp emp);
    public Emp select(int id);
    public int update(Emp emp);
}
