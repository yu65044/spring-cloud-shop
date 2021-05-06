package com.gupaoedu.vip.mall.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.gupaoedu.vip.mall.seckill.service.SeckillOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@RocketMQMessageListener(
        topic = "order-queue",
        consumerGroup = "orderqueue-consumer",
        selectorExpression = "*"
)
@Component
public class OrderQueueListener implements RocketMQListener {

    @Autowired
    private SeckillOrderService seckillOrderService;

    /***
     * 消息监听
     * @param message
     */
    @Override
    public void onMessage(Object message) {
        int count = seckillOrderService.add(JSON.parseObject(message.toString(), Map.class));
        //根据count判断抢单是否成功
        //如果成功了，则将数据推送到消息平台->和客户端进行通信的消息平台->Netty+Weboscket
    }
}
