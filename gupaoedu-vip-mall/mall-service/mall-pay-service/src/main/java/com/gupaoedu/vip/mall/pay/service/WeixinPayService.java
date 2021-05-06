package com.gupaoedu.vip.mall.pay.service;

import com.gupaoedu.vip.mall.pay.model.PayLog;

import java.util.Map;

public interface WeixinPayService {

    /***
     * 退款申请操作
     */
    Map<String,String> refund(Map<String,String> dataMap) throws Exception;

    // 预支付订单创建方法-获取支付地址
    Map<String,String> preOrder(Map<String,String> dataMap) throws Exception;

    /****
     * 主动查询支付结果
     * outno:订单编号
     */
    PayLog result(String outno) throws Exception;
}
