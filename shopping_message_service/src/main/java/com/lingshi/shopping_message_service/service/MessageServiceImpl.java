package com.lingshi.shopping_message_service.service;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.lingshi.shopping_common.constant.Const;
import com.lingshi.shopping_common.exception.BusCodeEnum;
import com.lingshi.shopping_common.exception.BusException;
import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IMessageService;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

@DubboService
public class MessageServiceImpl implements IMessageService {


    @Value("${message.accessKeyId}")
    private String accessKeyId;

    @Value("${message.accessKeySecret}")
    private String accessKeySecret;

    @Override
    @SneakyThrows
    public BaseResult sendMessage(String phoneNumber, String code) {


        com.aliyun.dysmsapi20170525.Client client = createClient();
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName("零食商城")
                .setTemplateCode("SMS_468750294")
                .setPhoneNumbers(phoneNumber)
                .setTemplateParam("{\"code\":\"" + code + "\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
//            {
//                "Message": "OK",
//                    "RequestId": "0BC2D9FE-C49C-5D42-AEF2-1F48E2FD8A0C",
//                    "Code": "OK",
//                    "BizId": "916209019801773517^0"
//            }
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            String responseCode = response.getBody().getCode();
            if (!Const.OK.equals(responseCode)) {
                return BaseResult.error(response.getBody().getMessage());
            }
        } catch (Exception error) {
            BusException.busException(BusCodeEnum.SEND_MESSAGE_ERROR);
        }
        return BaseResult.success();
    }


    /**
     * <b>description</b> :
     * <p>使用AK&amp;SK初始化账号Client</p>
     *
     * @return Client
     * @throws Exception
     */
    public com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(accessKeyId)
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

}
