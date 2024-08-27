package com.lingshi.shopping_order_customer_api.controller;


import com.lingshi.shopping_common.entity.Address;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IAddressService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 地址管理
 */
@RestController
@RequestMapping("/user/address")
public class AddressController {

    @DubboReference
    private IAddressService addressService;

    /**
     * 查询所有的省
     * @return
     */
    @GetMapping("/findAllProvince")
    public BaseResult findAllProvince(){
        return BaseResult.success(addressService.findAllProvince());
    }

    /**
     * 根据省id查询城市
     * @param provinceId 省id
     * @return
     */
    @GetMapping("/findCityByProvince")
    public BaseResult findCityByProvince(Long provinceId){
        return BaseResult.success(addressService.findCityByProvince(provinceId));
    }

    /**
     * 根据城市id查询区域
     * @param cityId 城市id
     * @return
     */
    @GetMapping("/findAreaByCity")
    public BaseResult findAreaByCity(Long cityId){
        return BaseResult.success(addressService.findAreaByCity(cityId));
    }

    /**
     * 根据id查询地址
     * @param id 地址id
     * @return
     */
    @GetMapping("/findById")
    public BaseResult findById(Long id){
        return BaseResult.success(addressService.findById(id));
    }

    /**
     * 根据用户id查询所有的地址
     * @param userId 用户id
     * @return
     */
    @GetMapping("/findByUser")
    public BaseResult findByUser(Long userId){
        return BaseResult.success(addressService.findByUser(userId));
    }

    /**
     * 新增地址
     * @param address 地址
     * @param userId 用户id
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Address address,@RequestHeader Long userId){
        address.setUserId(userId);
        addressService.add(address);
        return BaseResult.success();
    }

    /**
     * 修改地址
     * @param address 地址
     * @param userId 用户id
     * @return
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Address address,@RequestHeader Long userId){
        address.setUserId(userId);
        addressService.update(address);
        return BaseResult.success();
    }

    /**
     * 根据id删除地址
     * @param id 地址id
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long id){
        addressService.delete(id);
        return BaseResult.success();
    }


}