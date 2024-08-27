package com.lingshi.shopping_order_service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingshi.shopping_common.entity.Address;
import com.lingshi.shopping_common.entity.Area;
import com.lingshi.shopping_common.entity.City;
import com.lingshi.shopping_common.entity.Province;
import com.lingshi.shopping_common.service.IAddressService;
import com.lingshi.shopping_order_service.mapper.AddressMapper;
import com.lingshi.shopping_order_service.mapper.AreaMapper;
import com.lingshi.shopping_order_service.mapper.CityMapper;
import com.lingshi.shopping_order_service.mapper.ProvinceMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AddressServiceImpl implements IAddressService {

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Province> findAllProvince() {
        return provinceMapper.selectList(null);
    }

    @Override
    public List<City> findCityByProvince(Long provinceId) {
        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(City::getProvinceid,provinceId);
        return cityMapper.selectList(queryWrapper);
    }

    @Override
    public List<Area> findAreaByCity(Long cityId) {
        LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Area::getCityId,cityId);
        return areaMapper.selectList(queryWrapper);
    }

    @Override
    public void add(Address address) {
        addressMapper.insert(address);
    }

    @Override
    public void update(Address address) {
        addressMapper.updateById(address);
    }

    @Override
    public Address findById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    public void delete(Long id) {
        addressMapper.deleteById(id);
    }

    @Override
    public List<Address> findByUser(Long userId) {
        LambdaQueryWrapper<Address> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Address::getUserId,userId);
        List<Address> addresses = addressMapper.selectList(queryWrapper);
        return addresses;
    }
}
