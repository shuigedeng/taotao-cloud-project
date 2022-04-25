package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service.impl;


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
import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.mapper.RechargeMapper;
import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.pojo.Recharge;
import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by jackiechan on 2020-04-06 03:15
 *
 * @author shuigedeng
 */
@Service
public class RechargeServiceImpl implements RechargeService {
    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    public void addRecharge(Recharge recharge) throws Exception {
        rechargeMapper.insertRecharge(recharge);
    }

    @Override
    public PageInfo<Recharge> getRechargeList(Recharge criteria, int page, int limit) {
        PageHelper.startPage(page, limit);
        List<Recharge> rechargeList = rechargeMapper.getAllRecharges();
        return new PageInfo<>(rechargeList);
    }

    @Override
    public void updateRecharge(Recharge recharge) {
        recharge.setUpdatetime(new Date());
        rechargeMapper.updateRecharge(recharge);
    }

    @Override
    public void deleteRecharge(int[] ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            Recharge recharge = rechargeMapper.getRechargeMapById(id);
            if (recharge != null) {
                recharge.setState(0);
                rechargeMapper.updateRecharge(recharge);
            }
        }
    }

    @Override
    public Recharge getRechargeById(int id) {
        return rechargeMapper.getRechargeMapById(id);
    }
}
