package com.gupaoedu.vip.mall.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gupaoedu.vip.mall.pay.model.PayLog;

/*****
 * @Author:
 * @Description:
 ****/
public interface PayLogService extends IService<PayLog> {

    void add(PayLog payLog);
}
