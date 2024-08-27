package com.lingshi.shopping_pay_service.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.lingshi.shopping_common.entity.Orders;
import com.lingshi.shopping_common.entity.Payment;
import com.lingshi.shopping_common.exception.BusCodeEnum;
import com.lingshi.shopping_common.exception.BusException;
import com.lingshi.shopping_common.service.IZfbPayService;
import com.lingshi.shopping_pay_service.config.ZfbPayConfig;
import com.lingshi.shopping_pay_service.mapper.PaymentMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@DubboService
@Slf4j
public class ZfbPayServiceImpl implements IZfbPayService {

    @Autowired
    private ZfbPayConfig zfbPayConfig;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private PaymentMapper paymentMapper;


    @Override
    public String pcPay(Orders orders) {


        // 构造请求参数以调用接口
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        // 设置订单标题
        model.setSubject(orders.getCartGoods().get(0).getGoodsName());
        // 设置商户订单号
        model.setOutTradeNo(orders.getId());

        // 设置订单总金额
        model.setTotalAmount(orders.getPayment().toString());

        //将数据模型设置给请求对象
        request.setBizModel(model);

        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("调用成功=>{}", response.getBody());
                return response.getQrCode();
            } else {
                log.info("调用失败=>{}", response.getBody());
                BusException.busException(BusCodeEnum.ZFG_PAY_ERROR);
            }
        } catch (AlipayApiException e) {
            BusException.busException(BusCodeEnum.ZFG_PAY_ERROR);
        }
        return null;
    }


    @Override
    public boolean checkSign(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String,String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];
            params.put(key,value);
        }

        try {
            return AlipaySignature.rsaCertCheckV1(params,zfbPayConfig.getPublicKey(),"UTF-8","RSA2");
        } catch (AlipayApiException e) {
            log.error("",e);
            BusException.busException(BusCodeEnum.CHECK_ZFB_SIGNATURE_ERROR);
        }
        return false;
    }

    @Override
    public void addPayment(Payment payment) {
        paymentMapper.insert(payment);
    }
}