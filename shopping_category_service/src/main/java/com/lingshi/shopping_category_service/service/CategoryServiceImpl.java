package com.lingshi.shopping_category_service.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.constant.RedisKey;
import com.lingshi.shopping_common.entity.Category;
import com.lingshi.shopping_common.service.ICategoryService;
import com.lingshi.shopping_category_service.mapper.CategoryMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@DubboService
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public void add(Category category) {
        categoryMapper.insert(category);
        syncCategoryToRedis();
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
        syncCategoryToRedis();
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);

        categoryMapper.updateById(category);
        syncCategoryToRedis();
    }

    @Override
    public void delete(Long[] ids) {
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Category::getId,ids);
        categoryMapper.delete(updateWrapper);
        syncCategoryToRedis();

    }

    @Override
    public Category findById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public Page<Category> search(int page, int size) {
        return categoryMapper.selectPage(new Page<>(page,size),null);
    }

    @Override
    public List<Category> findAll() {
        //获取redis操作list集合的对象
        ListOperations<String,Category> los = redisTemplate.opsForList();

        List<Category> categorys = los.range(RedisKey.CATEGORYS,0,-1);

        if (CollectionUtils.isNotEmpty(categorys)){
            System.out.println("=============从redis获取==========");
            return categorys;
        }else {
            System.out.println("===============从数据中获取=============");
        }

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,1);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return categoryMapper.selectList(queryWrapper);
    }
    /**
     * 同步Mysql数据到Redis
     */
    public void syncCategoryToRedis(){
        //从数据库查询
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,1);
        List<Category> categories = categoryMapper.selectList(queryWrapper);

        ListOperations<String,Category> los = redisTemplate.opsForList();
        //删除redis原有的数据-删除key就删除数据
        redisTemplate.delete(RedisKey.CATEGORYS);

        //将数据存储到redis
        los.leftPushAll(RedisKey.CATEGORYS,categories);

    }
}
