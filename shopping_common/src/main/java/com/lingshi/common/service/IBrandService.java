package com.lingshi.common.service;

import com.lingshi.common.entity.Brand;
import com.lingshi.common.result.BaseResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jkl
 * @since 2024-06-26
 */
public interface IBrandService {
    Brand findById(Long id);

}
