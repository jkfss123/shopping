package com.lingshi.shopping_goods_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingshi.shopping_common.entity.Goods;
import com.lingshi.shopping_common.entity.GoodsDesc;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper extends BaseMapper<Goods> {
    void addSpecificationOption(@Param("gid") Long gid,@Param("optionId") Long optionId);

    @Delete("delete  t_goods_specification_option where gid = #{gid}")
    void deleteSpecificationOptionByGoodsId(Long id);

    Goods findById(Long id);

    /**
     * 查询所有的数据封装成ES需要数据对象
     */
    List<GoodsDesc> findAll();

    GoodsDesc findGoodDescById(Long id);
}
