package com.taotao.cloud.java.javaee.s1.c11_web.java.controller;


//
//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖镇楼                  BUG辟易  
//          佛曰:  
//                  写字楼里写字间，写字间里程序员；  
//                  程序人员写程序，又拿程序换酒钱。  
//                  酒醒只在网上坐，酒醉还来网下眠；  
//                  酒醉酒醒日复日，网上网下年复年。  
//                  但愿老死电脑间，不愿鞠躬老板前；  
//                  奔驰宝马贵者趣，公交自行程序员。  
//                  别人笑我忒疯癫，我笑自己命太贱；  


import com.github.pagehelper.PageInfo;
import com.taotao.cloud.java.javaee.s1.c11_web.java.bean.AjaxMessage;
import com.taotao.cloud.java.javaee.s1.c11_web.java.bean.TableData;
import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.Customer;
import com.taotao.cloud.java.javaee.s1.c11_web.java.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jackiechan on 2020-04-05 04:48
 *
 * @author shuigedeng
 */
@RestController
@RequestMapping("/sys/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;


    @RequestMapping("/add")
    public AjaxMessage add(Customer customer) {
        try {
            customer.setMoney(customer.getMoney()*10000);
            customerService.addCustomer(customer);
            return new AjaxMessage(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "添加失败");
    }


    @RequestMapping("/table")
    public TableData table(Customer customer, Integer page, Integer limit) {
        PageInfo<Customer> pageInfo = customerService.getCustomerList(customer, page, limit);
        return new TableData(pageInfo.getTotal(), pageInfo.getList());
    }

    @RequestMapping("/update")
    public AjaxMessage update(Customer Customer) {
        try {

            customerService.updateCustomer(Customer);
            return new AjaxMessage(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "添加失败");
    }

    @RequestMapping("/del")
    public AjaxMessage delete(int[] ids) {
        try {
            customerService.deleteCustomer(ids);
            return new AjaxMessage(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "删除失败");
    }

    @RequestMapping("/info")
    public Customer info(int id) {
        return customerService.getCustomerById(id);
    }

    @RequestMapping("/tree")//获取所有客户列表,添加应用时使用,实际开发中不需要
    public List<Customer> tree() {
        return customerService.getAllCustomer();
    }
}
