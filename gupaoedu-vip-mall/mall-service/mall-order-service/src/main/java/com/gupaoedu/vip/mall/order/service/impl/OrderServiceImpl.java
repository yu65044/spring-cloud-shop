package com.gupaoedu.vip.mall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.cart.feign.CartFeign;
import com.gupaoedu.vip.mall.cart.model.Cart;
import com.gupaoedu.vip.mall.goods.feign.SkuFeign;
import com.gupaoedu.vip.mall.order.mapper.OrderMapper;
import com.gupaoedu.vip.mall.order.mapper.OrderRefundMapper;
import com.gupaoedu.vip.mall.order.mapper.OrderSkuMapper;
import com.gupaoedu.vip.mall.order.model.Order;
import com.gupaoedu.vip.mall.order.model.OrderRefund;
import com.gupaoedu.vip.mall.order.model.OrderSku;
import com.gupaoedu.vip.mall.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderSkuMapper orderSkuMapper;

    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    /****
     * 退款申请
     * @param orderRefund
     * @return
     */
    @Override
    public int refund(OrderRefund orderRefund) {
        //1记录退款申请
        orderRefundMapper.insert(orderRefund);

        //2修改订单状态
        Order order = new Order();
        order.setOrderStatus(4);    //申请退款

        //构建条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>();
        queryWrapper.eq("id",orderRefund.getOrderNo()); //订单ID
        queryWrapper.eq("username",orderRefund.getUsername()); //用户名
        queryWrapper.eq("order_status",1);
        queryWrapper.eq("pay_status",1);
        int count = orderMapper.update(order, queryWrapper);
        return count;
    }

    /***
     * 添加订单
     */
    @GlobalTransactional
    @Override
    public Boolean add(Order order) {
        //数据完善
        order.setId(IdWorker.getIdStr());   //ID
        order.setCreateTime(new Date());    //创建时间
        order.setUpdateTime(order.getCreateTime());

        //1、查询购物车数据
        RespResult<List<Cart>> cartResp = cartFeign.list(order.getCartIds());
        List<Cart> carts = cartResp.getData();
        if(carts==null || carts.size()==0){
            return false;
        }

        //2、递减库存
        skuFeign.dcount(carts);

        //3、添加订单明细
        int totalNum=0;
        int moneys = 0;
        for (Cart cart : carts) {
            //将Cart转成OrderSku
            OrderSku orderSku = JSON.parseObject(JSON.toJSONString(cart), OrderSku.class);
            orderSku.setId(IdWorker.getIdStr());
            orderSku.setOrderId(order.getId()); //提前赋值
            orderSku.setMoney(orderSku.getPrice()*orderSku.getNum());

            //添加
            orderSkuMapper.insert(orderSku);

            //统计计算
            totalNum +=orderSku.getNum();
            moneys += orderSku.getMoney();
        }

        //4、添加订单
        order.setTotalNum(totalNum);
        order.setMoneys(moneys);
        orderMapper.insert(order);

        //Exception--->TestTransaction
        //int q=10/0;

        //5、删除购物车数据
        cartFeign.delete(order.getCartIds());
        return true;
    }

    /****
     * 支付成功后状态修改
     * @param id
     * @return
     */
    @Override
    public int updateAfterPayStatus(String id) {
        //修改后的状态
        Order order = new Order();
        order.setId(id);
        order.setOrderStatus(1);    // 待发货
        order.setPayStatus(1);  //已支付

        //修改条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>();
        queryWrapper.eq("id",id);
        queryWrapper.eq("order_status",0);
        queryWrapper.eq("pay_status",0);
        return orderMapper.update(order,queryWrapper);
    }
}
