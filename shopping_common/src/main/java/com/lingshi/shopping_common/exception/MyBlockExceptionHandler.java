package com.lingshi.shopping_common.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.fastjson.JSON;
import com.lingshi.shopping_common.service.result.BaseResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class MyBlockExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        if(e instanceof FlowException){
            BaseResult baseResult = BaseResult.error(BusCodeEnum.SYSTEM_ERROR.getMessage());
            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.getWriter().println(JSON.toJSONString(baseResult));
        }
    }
}

