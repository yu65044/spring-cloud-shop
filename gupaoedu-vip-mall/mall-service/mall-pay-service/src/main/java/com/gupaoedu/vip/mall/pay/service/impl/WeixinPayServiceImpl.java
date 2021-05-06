package com.gupaoedu.vip.mall.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.gupaoedu.vip.mall.pay.config.WeixinPayConfig;
import com.gupaoedu.vip.mall.pay.mapper.PayLogMapper;
import com.gupaoedu.vip.mall.pay.model.PayLog;
import com.gupaoedu.vip.mall.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    private WXPay wxPay;

    @Autowired
    private PayLogMapper payLogMapper;

    /****
     * 申请退款
     * @param dataMap
     * @return
     */
    @Override
    public Map<String, String> refund(Map<String, String> dataMap) throws Exception {
        return wxPay.refund(dataMap);
    }

    /****
     * 预支付下单操作（获取支付二维码扫码地址）
     * @param dataMap
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> preOrder(Map<String, String> dataMap) throws Exception {
        return wxPay.unifiedOrder(dataMap);
    }


    /****
     * 支付结果查询
     * @param outno
     * @return
     * @throws Exception
     */
    @Override
    public PayLog result(String outno) throws Exception {
        //查询数据库中支付日志
        PayLog payLog = payLogMapper.selectById(outno);

        if(payLog==null){
            //数据库中没有数据查询微信支付服务
            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no",outno);
            Map<String, String> resp = wxPay.orderQuery(data);
            //把支付结果存入数据库中（不可逆转支付结果）
            //return_code result_code  trade_state
            String tradeState = resp.get("trade_state");
            int status = tradeState(tradeState);

            //不可逆转的支付状态，记录日志
            if(status==2 || status==3 || status==4 || status==5 || status==7){
                payLog = new PayLog(outno,status, JSON.toJSONString(resp),outno,new Date());
                payLogMapper.insert(payLog);
            }
        }
        return payLog;
    }

    /***
     * 支付状态
     * @param tradeState
     * @return
     */
    public int tradeState(String tradeState){
        int state = 1;
        switch (tradeState){
            case "NOTPAY":  //未支付
                state = 1;
                break;
            case "SUCCESS":
                state = 2;  //已支付
                break;
            case "REFUND":
                state = 3;  //转入退款
                break;
            case "CLOSED":
                state = 4;  //已关闭
                break;
            case "REVOKED":
                state = 5;  //已撤销
                break;
            case "USERPAYING":
                state = 6;  //用户支付中
                break;
            case "PAYERROR":
                state = 7;  //支付失败
                break;
            default:
                state=1;
        }
        return state;
    }
}
