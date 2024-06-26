package org.example.service.impl;

import org.example.entity.City;
import org.example.mapper.CityMapper;
import org.example.service.ICityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 行政区域地州市信息表 服务实现类
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements ICityService {

}
