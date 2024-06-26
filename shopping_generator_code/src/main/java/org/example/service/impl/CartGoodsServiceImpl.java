package org.example.service.impl;

import org.example.entity.CartGoods;
import org.example.mapper.CartGoodsMapper;
import org.example.service.ICartGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
@Service
public class CartGoodsServiceImpl extends ServiceImpl<CartGoodsMapper, CartGoods> implements ICartGoodsService {

}
