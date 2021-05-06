package com.gupaoedu.vip.mall.order.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gupaoedu.mall.util.RespCode;
import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.order.model.Order;
import com.gupaoedu.vip.mall.order.model.OrderRefund;
import com.gupaoedu.vip.mall.order.service.OrderService;
import com.gupaoedu.vip.mall.pay.WeixinPayParam;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeixinPayParam weixinPayParam;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /****
     * 申请取消订单（模拟测试退款的订单）
     */
    @PutMapping(value = "/refund/{id}")
    public RespResult refund(@PathVariable(value = "id")String id,HttpServletRequest request) throws Exception{
        //用户名
        String username = "gp";
        //查询订单，是否符合退款要求
        Order order = orderService.getById(id);
        if(order.getPayStatus().intValue()==1 && order.getOrderStatus().intValue()==1){
            //添加退款记录,更新订单状态
            OrderRefund orderRefund = new OrderRefund(
                    IdWorker.getIdStr(),
                    id,
                    1,
                    null,
                    username,
                    0,//申请退款
                    new Date(),
                    order.getMoneys()
            );
            orderService.refund(orderRefund);

            //向MQ发消息（申请退款）  out_trade_no（订单号）  out_refund_no（退款订单号）  total_fee（订单金额）  refund_fee（退款金额）
            Message message = MessageBuilder.withPayload(weixinPayParam.weixinRefundParam(orderRefund)).build();
            TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("refundtx", "refund", message, orderRefund);

            if(transactionSendResult.getSendStatus()== SendStatus.SEND_OK){
                return RespResult.ok();
            }
        }
        //不符合直接返回错误
        return RespResult.error("当前订单不符合取消操作要求！");
    }


    /***
     * 添加订单
     */
    @PostMapping
    public RespResult add(@RequestBody Order order, HttpServletRequest request) throws Exception {
        //用户名字
        order.setUsername("gp");
        //下单
        Boolean bo = orderService.add(order);
        String ciptxt = weixinPayParam.weixinParam(order, request);
        return bo? RespResult.ok(ciptxt) : RespResult.error(RespCode.ERROR);
    }


}
