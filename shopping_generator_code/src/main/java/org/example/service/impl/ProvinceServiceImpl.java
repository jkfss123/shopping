package org.example.service.impl;

import org.example.entity.Province;
import org.example.mapper.ProvinceMapper;
import org.example.service.IProvinceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 省份信息表 服务实现类
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
@Service
public class ProvinceServiceImpl extends ServiceImpl<ProvinceMapper, Province> implements IProvinceService {

}
