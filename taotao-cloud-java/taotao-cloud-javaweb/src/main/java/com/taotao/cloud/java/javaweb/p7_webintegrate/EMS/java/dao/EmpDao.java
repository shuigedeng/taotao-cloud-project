package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.dao;


import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.entity.Emp;
import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.entity.Page;
import java.util.List;

public interface EmpDao {
    public List<Emp> selectAll(Page page);
    public long selectCount();
    public int delete(int id);
    public int insert(Emp emp);
    public Emp select(int id);
    public int update(Emp emp);
}
