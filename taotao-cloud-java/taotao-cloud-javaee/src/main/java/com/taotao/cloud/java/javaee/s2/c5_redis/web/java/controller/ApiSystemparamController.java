package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.controller;


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
import com.qianfeng.openapi.web.master.pojo.ApiSystemparam;
import com.qianfeng.openapi.web.master.bean.TableData;
import com.qianfeng.openapi.web.master.service.ApiSystemparamService;
import com.qianfeng.openapi.web.master.bean.AjaxMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jackiechan on 2020-03-31 23:07
 *
 * @Author jackiechan
 */
@RestController
@RequestMapping("/sys/systemparaters")
public class ApiSystemparamController {
    @Autowired
    private ApiSystemparamService apiSystemparamService;


    @RequestMapping("/add")
    public AjaxMessage add(ApiSystemparam systemparam) {
        try {

            apiSystemparamService.addApiSystemparam(systemparam);
            return new AjaxMessage(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "添加失败");
    }


    @RequestMapping("/table")
    public TableData table(ApiSystemparam apiMapping, Integer page, Integer limit) {
        PageInfo<ApiSystemparam> pageInfo = apiSystemparamService.getSystemparamList(apiMapping, page, limit);
        return new TableData(pageInfo.getTotal(), pageInfo.getList());
    }

    @RequestMapping("/update")
    public AjaxMessage update(ApiSystemparam systemparam) {
        try {

            apiSystemparamService.updateApiSystemparam(systemparam);
            return new AjaxMessage(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "添加失败");
    }

    @RequestMapping("/del")
    public AjaxMessage delete(int[] ids) {
        try {
            apiSystemparamService.deleteSystemparam(ids);
            return new AjaxMessage(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxMessage(false, "删除失败");
    }

    @RequestMapping("/info")
    public ApiSystemparam info(int id) {
        return apiSystemparamService.getSystemparamById(id);
    }
}
