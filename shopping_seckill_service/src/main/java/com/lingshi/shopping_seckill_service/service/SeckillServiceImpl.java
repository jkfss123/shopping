package com.lingshi.shopping_seckill_service.service;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.constant.RedisKey;
import com.lingshi.shopping_common.entity.CartGoods;
import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.entity.SeckillGoods;
import com.lingshi.shopping_common.exception.BusCodeEnum;
import com.lingshi.shopping_common.exception.BusException;
import com.lingshi.shopping_common.exception.OrderStatusEnums;
import com.lingshi.shopping_common.service.ISeckillService;
import com.lingshi.shopping_seckill_service.mapper.SeckillGoodsMapper;
import com.lingshi.shopping_seckill_service.redis.RedissonLock;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@DubboService
@Service
public class SeckillServiceImpl implements ISeckillService {


    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BitMapBloomFilter bloomFilter;

    @Autowired
    private RedissonLock redissonLock;


    /**
     * 同步MySQL中的秒杀商品到Redis
     * 使用SpringBoot中的定时器
     * 秒杀商品秒杀时间不定，当前时间不再秒杀时间范围内的踢出，新的秒杀商品在时间当前时间范围内加入进来
     * 每一分钟从MySQL同步到 Redis一次
     * <p>
     * cron 定时器表达式 6位
     * 秒 分 时 日 月 周
     * * ：表示任意时刻
     * <p>
     * startTime < now  < endTime
     */
    //@Scheduled(cron = "0 * * * * *")
    private void refreshSeckillGoodsToRedis() {
        //将redis数据（库存）同步到MySQL
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps(RedisKey.SECOND_KILL_GOODS).values();
        if (CollUtil.isNotEmpty(seckillGoodsList)) {
            for (SeckillGoods redisSeckillGoods : seckillGoodsList) {
                SeckillGoods sqlSeckillGoods = seckillGoodsMapper.selectById(redisSeckillGoods.getId());
                //只有redis库存和mysql库存不相同同步一下
                if (!Objects.equals(redisSeckillGoods.getStockCount(), sqlSeckillGoods.getStockCount())) {
                    //修改库存
                    sqlSeckillGoods.setStockCount(redisSeckillGoods.getStockCount());
                    //同步mysql
                    seckillGoodsMapper.updateById(sqlSeckillGoods);
                }
            }
        }

        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println("now = " + now);
        LambdaQueryWrapper<SeckillGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(SeckillGoods::getStartTime, now);
        queryWrapper.gt(SeckillGoods::getEndTime, now);
        List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectList(queryWrapper);

        //删除之前商品
        redisTemplate.delete(RedisKey.SECOND_KILL_GOODS);

        if (CollUtil.isNotEmpty(seckillGoods)) {
            for (SeckillGoods seckillGood : seckillGoods) {
                addSeckillGoodsToRedis(seckillGood);
                //将商品id添加布隆过滤器
                bloomFilter.add(String.valueOf(seckillGood.getGoodsId()));
            }
        }

    }

    @Override
    public void addSeckillGoodsToRedis(SeckillGoods seckillGoods) {
        redisTemplate.boundHashOps(RedisKey.SECOND_KILL_GOODS).put(seckillGoods.getGoodsId(), seckillGoods);
    }

    @Override
    public Page<SeckillGoods> findPageByRedis(int page, int size) {

        //redis查询所有的秒杀商品集合
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps(RedisKey.SECOND_KILL_GOODS).values();

        //起始索引
        int start = (page - 1) * size;

        //计算结束索引(三目运算)
        int end = (start + size) > seckillGoodsList.size() ? seckillGoodsList.size() : start + size;

        //截取当前页数据
        List<SeckillGoods> records = seckillGoodsList.subList(start, end);
        //创建分页对象
        Page<SeckillGoods> seckillGoodsPage = new Page<>();
        seckillGoodsPage.setCurrent(page);//当前页
        seckillGoodsPage.setTotal(seckillGoodsList.size());//总记录
        seckillGoodsPage.setSize(size);//每页条数
        seckillGoodsPage.setRecords(records);//当前页数据

        //故意延迟响应时间500ms
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return seckillGoodsPage;
    }

    @Override
    public SeckillGoods findSeckillGoodsByRedis(Long goodsId) {

//        if(!bloomFilter.contains(String.valueOf(goodsId))){
//            System.out.println("布隆过滤器中没有此商品==================");
//            return null;
//        }

        //从redis查询订单详情
        Object obj = redisTemplate.boundHashOps(RedisKey.SECOND_KILL_GOODS).get(goodsId);
        if (Objects.nonNull(obj)) {
            System.out.println("redis查询=============");
            return (SeckillGoods) obj;
        }
        return null;
    }

    @SentinelResource(value = "findSeckillGoodsByMySQL", blockHandler = "blockHandler")
    public SeckillGoods findSeckillGoodsByMySQL(Long goodsId) {
        System.out.println("MySQL查询查询=============");
        LambdaQueryWrapper<SeckillGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SeckillGoods::getGoodsId, goodsId);
        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        queryWrapper.lt(SeckillGoods::getStartTime, now);
        queryWrapper.gt(SeckillGoods::getEndTime, now);

        SeckillGoods seckillGoods = seckillGoodsMapper.selectOne(queryWrapper);
        if (Objects.nonNull(seckillGoods)) {
            //同步到Redis
            addSeckillGoodsToRedis(seckillGoods);
        }

        return seckillGoods;
    }

    public SeckillGoods blockHandler(Long goodsId, BlockException e) {
        System.out.println("限流降级了....");
        return null;
    }

    @Override
    public Orders createOrder(Orders orders) {
        //使用商品的id作为秒杀锁的key
        Long goodId = orders.getCartGoods().get(0).getGoodId();

        try {
            //操作加锁
            if (redissonLock.lock(String.valueOf(goodId), 5000L)) {
                //设置订单id
                //String snowflakeNextIdStr = IdUtil.getSnowflakeNextIdStr();
                orders.setId(IdWorker.getIdStr());

                //下单时间
                orders.setCreateTime(LocalDateTime.now());
                //计算总价
                CartGoods goods = orders.getCartGoods().get(0);
                //要买的数量
                Integer num = goods.getNum();
                BigDecimal price = goods.getPrice();

                BigDecimal paytment = price.multiply(new BigDecimal(num));
                //设置订单总价
                orders.setPayment(paytment);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                SeckillGoods redisGoods = findSeckillGoodsByRedis(goods.getGoodId());
                //判断库存是否足够
                if (redisGoods.getStockCount() < 1 || num > redisGoods.getStockCount()) {
                    BusException.busException(BusCodeEnum.SECOND_KILL_GOODS_STOCK_COUNT_ERROR);
                }
                //
                redisGoods.setStockCount(redisGoods.getStockCount() - num);
                //将修改库存的商品重新设置到redis
                addSeckillGoodsToRedis(redisGoods);
                //下单：将数据保存到Redis (订单id作为key，订单对象作为值)
                redisTemplate.opsForValue().set(orders.getId(), orders, Duration.ofMinutes(1));
                /**
                 * 创建订单副本，时间比订单要长一点
                 * 因为redis订单过期，需要将订单数据存储到MySQL，并设置订单状态为关闭
                 * 但是redis过期监听只能活到Redis的key，不能获取值
                 * 当监听redis订单key过期时候，获取订单副本的数据即可，同步Redis数据到MySQL以后，删除副本订单即可
                 */
                redisTemplate.opsForValue().set(StrUtil.format(RedisKey.SECOND_KILL_ORDER_COPY, orders.getId()), orders, Duration.ofMinutes(1));

                return orders;
            }
        } finally {
            //释放锁
            redissonLock.unlock(String.valueOf(goodId));
        }

        return null;
    }

    @Override
    public Orders findOrder(String id) {
        return (Orders) redisTemplate.opsForValue().get(id);
    }

    @Override
    public Orders pay(String orderId) {

        Orders orders = (Orders) redisTemplate.opsForValue().get(orderId);
        if (Objects.isNull(orders)) {
            BusException.busException(BusCodeEnum.SECOND_KILL_ORDER_EXIPRE);
        }
        //设置订单状态
        orders.setStatus(OrderStatusEnums.OK_PAY.getType());
        //设置支付时间
        orders.setPaymentTime(LocalDateTime.now());

        orders.setPaymentType("支付宝");
        //删除redis订单
        redisTemplate.delete(orderId);
        //删除副本订单
        redisTemplate.delete(StrUtil.format(RedisKey.SECOND_KILL_ORDER_COPY, orderId));

        return orders;
    }
}
