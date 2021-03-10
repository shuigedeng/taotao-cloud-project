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
import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.Recharge;
import com.taotao.cloud.java.javaee.s1.c11_web.java.service.CustomerService;
import com.taotao.cloud.java.javaee.s1.c11_web.java.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jackiechan on 2020-04-06 03:26
 *
 * @Author jackiechan
 */
@RestController
@RequestMapping("/sys/recharge")
public class RechargeController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RechargeService rechargeService;


    @RequestMapping( "/table")
    public TableData table(Recharge recharge, Integer page, Integer limit) {
        PageInfo<Recharge> list = rechargeService.getRechargeList(recharge, page, limit);
        return new TableData(list.getTotal(), list.getList());
    }

    @RequestMapping("/add")
    public AjaxMessage add(Recharge recharge) {

        try {
            recharge.setMoney(recharge.getMoney()*10000);
            rechargeService.addRecharge(recharge);
            customerService.addMoney(recharge.getMoney(), recharge.getCusId());
            return new AjaxMessage(true, "添加成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new AjaxMessage(true, "添加失败");
    }

    @RequestMapping( "/update")
    public AjaxMessage update(Recharge recharge) {
        try {
            rechargeService.updateRecharge(recharge);
            return new AjaxMessage(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "修改失败");
    }

    @RequestMapping( "/info")
    public Recharge info(Integer id) {
        return rechargeService.getRechargeById(id);
    }
}
