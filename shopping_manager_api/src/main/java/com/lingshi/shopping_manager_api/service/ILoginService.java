package com.lingshi.shopping_manager_api.service;

import com.lingshi.shopping_common.service.result.BaseResult;

public interface ILoginService {
    BaseResult login(String username,String password);
}
