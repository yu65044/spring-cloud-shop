package com.gupaoedu.vip.mall.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gupaoedu.vip.mall.pay.mapper.PayLogMapper;
import com.gupaoedu.vip.mall.pay.model.PayLog;
import com.gupaoedu.vip.mall.pay.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*****
 * @Author:
 * @Description:
 ****/
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper,PayLog> implements PayLogService {

    @Autowired
    private PayLogMapper payLogMapper;

    /***
     * log
     * @param payLog
     */
    @Override
    public void add(PayLog payLog) {
        //删除
        payLogMapper.deleteById(payLog.getId());
        //增加
        payLogMapper.insert(payLog);
    }
}
