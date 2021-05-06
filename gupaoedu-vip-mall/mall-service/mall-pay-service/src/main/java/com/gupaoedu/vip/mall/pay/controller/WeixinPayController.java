package com.gupaoedu.vip.mall.pay.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.gupaoedu.mall.util.*;
import com.gupaoedu.vip.mall.pay.model.PayLog;
import com.gupaoedu.vip.mall.pay.service.WeixinPayService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/wx")
@CrossOrigin
public class WeixinPayController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private Signature signature;

    //秘钥->MD5（skey）
    @Value("${payconfig.weixin.key}")
    private String skey;

    /****
     * 查询订单支付状态
     */
    @GetMapping(value = "/result/{outno}")
    public RespResult<PayLog> result(@PathVariable(value = "outno")String outno) throws Exception{
        PayLog payLog = weixinPayService.result(outno);
        return RespResult.ok(payLog);
    }

    /****
     * 微信支付二维码获取
     */
    @GetMapping(value = "/pay")
    //public RespResult<Map> pay(@RequestParam Map<String,String> dataMap) throws Exception {
    public RespResult<Map> pay(@RequestParam("ciptext") String ciphertext) throws Exception {
        //ciphertext->AES->移除签名数据signature->MD5==signature?
        Map<String, String> dataMap = signature.security(ciphertext);

        Map<String, String> map = weixinPayService.preOrder(dataMap);
        if(map!=null){
            map.put("orderNumber",dataMap.get("out_trade_no"));
            map.put("money",dataMap.get("total_fee"));
            return RespResult.ok(map);
        }
        return RespResult.error("支付系统繁忙，请稍后再试！");
    }

    /****
     * 支付结果回调
     */
    @RequestMapping(value = "/result")
    public String result(HttpServletRequest request) throws Exception{
        //读取网络输入流
        ServletInputStream is = request.getInputStream();

        //定义接收输入流对象（输出流）
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        //将网络输入流读取到输出流中
        byte[] buffer = new byte[1024];
        int len=0;
        while ((len=is.read(buffer))!=-1){
            os.write(buffer,0,len);
        }

        //关闭资源
        os.close();
        is.close();

        //将支付结果的XML结构转换成Map结构
        String xmlResult = new String(os.toByteArray(),"UTF-8");
        Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
        System.out.println("xmlResult:"+xmlResult);
        //判断支付结果状态  日志状态：2 成功 ， 7 失败
        int status = 7;
        // return_code/result_code
        if(map.get("return_code").equals(WXPayConstants.SUCCESS) && map.get("result_code").equals(WXPayConstants.SUCCESS)){
            status=2;
        }

        //创建日志对象
        PayLog payLog = new PayLog(map.get("out_trade_no"),status,JSON.toJSONString(map),map.get("out_trade_no"),new Date());

        //构建消息
        Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(payLog)).build();
        //发消息
        rocketMQTemplate.sendMessageInTransaction("rocket","log",message,null);

        //Map 响应数据
        Map<String,String> resultResp = new HashMap<String,String>();
        resultResp.put("return_code","SUCCESS");
        resultResp.put("return_msg","OK");
        return WXPayUtil.mapToXml(resultResp);
    }


    /****
     * 申请退款状态
     */
    @RequestMapping(value = "/refund/result")
    public String refund(HttpServletRequest request) throws Exception{
        //读取网络输入流
        ServletInputStream is = request.getInputStream();

        //定义接收输入流对象（输出流）
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        //将网络输入流读取到输出流中
        byte[] buffer = new byte[1024];
        int len=0;
        while ((len=is.read(buffer))!=-1){
            os.write(buffer,0,len);
        }

        //关闭资源
        os.close();
        is.close();

        //将支付结果的XML结构转换成Map结构
        String xmlResult = new String(os.toByteArray(),"UTF-8");
        Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
        System.out.println("退款数据-xmlResult:"+xmlResult);

        //获取退款信息（加密了-AES）
        String reqinfo = map.get("req_info");
        String key = MD5.md5(skey);
        byte[] decode = AESUtil.encryptAndDecrypt(Base64Util.decode(reqinfo), key, 2);
        System.out.println("退款解密后的数据："+new String(decode, "UTF-8"));

        //Map 响应数据
        Map<String,String> resultResp = new HashMap<String,String>();
        resultResp.put("return_code","SUCCESS");
        resultResp.put("return_msg","OK");
        return WXPayUtil.mapToXml(resultResp);
    }
}
