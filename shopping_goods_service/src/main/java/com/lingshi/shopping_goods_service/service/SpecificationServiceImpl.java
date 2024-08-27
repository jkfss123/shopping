package com.lingshi.shopping_goods_service.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingshi.shopping_common.entity.Specification;
import com.lingshi.shopping_common.entity.SpecificationOption;
import com.lingshi.shopping_common.entity.SpecificationOptions;
import com.lingshi.shopping_common.service.ISpecificationService;
import com.lingshi.shopping_goods_service.mapper.SpecificationMapper;
import com.lingshi.shopping_goods_service.mapper.SpecificationOptionMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@DubboService
public class SpecificationServiceImpl implements ISpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public void add(Specification specification) {
        specificationMapper.insert(specification);

    }

    @Override
    public void update(Specification specification) {
        specificationMapper.updateById(specification);

    }

    @Override
    public Specification findById(Long id) {
        Specification specification = specificationMapper.findById(id);
        return specification;

    }

    @Override
    public void delete(Long id) {
        //删除规格选项之前判断这个选项是否还有别的商品使用
        //TODO

        //先删除商品规格选项
        LambdaUpdateWrapper<SpecificationOption> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SpecificationOption::getSpecId,id);
        specificationOptionMapper.delete(updateWrapper);

        specificationMapper.deleteById(id);
    }

    @Override
    public List<Specification> findByProductTypeId(Long productTypeId) {
        return specificationMapper.findByProductTypeId(productTypeId);
    }

    @Override
    public Page<Specification> search(int page, int size) {
        return specificationMapper.selectPage(new Page<>(page,size),null);
    }



    @Override
    public void addOption(SpecificationOptions specificationOptions) {
        String[] optionNames = specificationOptions.getOptionName();
        for (String optionName : optionNames){
            SpecificationOption option = new SpecificationOption();
            option.setSpecId(specificationOptions.getSpecId());
            option.setOptionName(optionName);
            specificationOptionMapper.insert(option);

        }

    }

    @Override
    public void deleteOption(Long[] ids) {
        specificationOptionMapper.deleteBatchIds(Arrays.asList(ids));

    }
}
