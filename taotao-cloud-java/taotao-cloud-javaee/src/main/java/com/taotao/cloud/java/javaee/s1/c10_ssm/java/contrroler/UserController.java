package com.taotao.cloud.java.javaee.s1.c10_ssm.java.contrroler;


import com.github.pagehelper.PageInfo;
import com.taotao.cloud.java.javaee.s1.c10_ssm.java.bean.BaseResultBean;
import com.taotao.cloud.java.javaee.s1.c10_ssm.java.bean.TableData;
import com.taotao.cloud.java.javaee.s1.c10_ssm.java.pojo.User;
import com.taotao.cloud.java.javaee.s1.c10_ssm.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/userlist")
    public TableData getUserList(int page, int limit) {
        PageInfo<User> pageInfo = userService.getUserList(page, limit);
        TableData tableData = new TableData();
        tableData.setCode(0);
        tableData.setMsg("成功");
        tableData.setCount(pageInfo.getTotal());//设置总条数
        tableData.setData(pageInfo.getList());//设置当前的数据
        return tableData;
    }
    @PostMapping("/del")
    public BaseResultBean delteUserByIds(int[] ids) {
        userService.delteUserByIds( ids);
        BaseResultBean baseResultBean = new BaseResultBean();
        baseResultBean.setMessage("成功");
        baseResultBean.setStatus(true);
        return baseResultBean;
    }
}
