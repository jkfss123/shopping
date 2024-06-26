package org.example.service.impl;

import org.example.entity.Area;
import org.example.mapper.AreaMapper;
import org.example.service.IAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 行政区域县区信息表 服务实现类
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {

}
