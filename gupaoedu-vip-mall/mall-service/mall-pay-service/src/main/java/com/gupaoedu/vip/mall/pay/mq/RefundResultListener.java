package com.gupaoedu.vip.mall.pay.mq;

import com.gupaoedu.mall.util.Signature;
import com.gupaoedu.vip.mall.pay.service.WeixinPayService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@Component
@RocketMQMessageListener(topic = "refund",consumerGroup = "refundgroup")
public class RefundResultListener implements RocketMQListener,RocketMQPushConsumerLifecycleListener {

    @Autowired
    private Signature signature;

    @Autowired
    private WeixinPayService weixinPayService;

    @Override
    public void onMessage(Object message) {
    }

    /****
     * 消息监听
     * @param consumer
     */
    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                    for (MessageExt msg : msgs) {
                        //AES加密字符串
                        String result = new String(msg.getBody(),"UTF-8");

                        //解密
                        Map<String, String> dataMap = signature.security(result);

                        //发送请求执行退款申请
                        Map<String, String> refundMap = weixinPayService.refund(dataMap);
                        System.out.println("退款申请状态---refundMap:"+refundMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
    }
}
