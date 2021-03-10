package com.taotao.cloud.java.javaee.s1.c11_web.java.service.impl;


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


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.java.javaee.s1.c11_web.java.mapper.ApiSystemparamMapper;
import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.ApiSystemparam;
import com.taotao.cloud.java.javaee.s1.c11_web.java.service.ApiSystemparamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jackiechan on 2020-04-05 02:32
 *
 * @Author jackiechan
 */
@Service
@Transactional
public class ApiSystemparamServiceImpl implements ApiSystemparamService {
    @Autowired
    private ApiSystemparamMapper systemparamDao;

    @Override
    public void addApiSystemparam(ApiSystemparam apiSystemparam) throws Exception {
        systemparamDao.insertApiSystemparam(apiSystemparam);
    }

    @Override
    public PageInfo<ApiSystemparam> getSystemparamList(ApiSystemparam criteria, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(systemparamDao.queryApiSystemparam(criteria));
    }

    @Override
    public void updateApiSystemparam(ApiSystemparam systemparam) {
        systemparamDao.updateApiSystemparam(systemparam);
    }

    @Override
    public void deleteSystemparam(int[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        for (int id : ids) {
            ApiSystemparam systemparam = systemparamDao.getMappingById(id);
            if (systemparam != null) {
                systemparam.setState(0);
                systemparamDao.updateApiSystemparam(systemparam);
            }
        }
    }

    @Override
    public ApiSystemparam getSystemparamById(int id) {
        return systemparamDao.getMappingById(id);
    }


}
