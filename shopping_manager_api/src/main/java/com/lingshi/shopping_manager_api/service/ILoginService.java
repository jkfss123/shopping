package com.lingshi.shopping_manager_api.service;

import com.lingshi.common.result.BaseResult;

public interface ILoginService {
    BaseResult login(String username,String password);
}
