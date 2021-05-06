package com.gupaoedu.vip.test;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.List;

public class BloomFilterTest {
    //集合大小
    private static int size = 1000000;

    //Google的布隆过滤器
    private static BloomFilter<Integer> bloomFilter =BloomFilter.create(Funnels.integerFunnel(), size,0.01);

    public static void main(String[] args) {
        //放一百万个key到布隆过滤器中
        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }

        List<Integer> list = new ArrayList<Integer>(1000);


        //取10000个不在过滤器里的值，看看有多少个会被认为在过滤器里
        for (int i = size + 1; i < size + 20000; i++) {
            //判断布隆过滤器是否存在该数据
            if (bloomFilter.mightContain(i)) {
                list.add(i);
            }
        }
        System.out.println("误判的数量：" + list.size());
    }
}
