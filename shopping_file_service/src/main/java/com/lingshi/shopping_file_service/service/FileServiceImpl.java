package com.lingshi.shopping_file_service.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.lingshi.shopping_common.service.IFileService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.List;

@Component
@DubboService
public class FileServiceImpl implements IFileService {

    /**
     * 服务端域名
     */
    @Value("${oss.upload.serverUrl}")
    private String serverUrl;
    /**
     * accessKey
     */
    @Value("${oss.upload.accessKey}")
    private String accessKeyId;
    /**
     * secretKey
     */
    @Value("${oss.upload.secretKey}")
    private String accessKeySecret;
    /**
     * bucket
     */
    @Value("${oss.upload.bucket}")
    private String bucketName;
    /**
     * region
     */
    @Value("${oss.upload.endpoint}")
    private String endpoint;

    public OSS initOss(){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);
        return ossClient;
    }
    @Override
    public String uploadImage(byte[] fileBytes, String fileName) {

        OSS oss = initOss();
        try {

            //获取文件名后缀
            String extName = FileNameUtil.extName(fileName);
            //生成新的随机文件名称
            String newFileName = RandomUtil.randomString(20);

            newFileName = newFileName + StrUtil.DOT + extName;

            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);

            //上传oss文件
            oss.putObject(bucketName, newFileName, inputStream);

            return serverUrl + StrUtil.SLASH + newFileName;

        }
        finally {
            oss.shutdown();
        }
    }

    @Override
    public void delete(String filePath) {
        OSS oss = initOss();
        try {
            List<String> list = StrUtil.split(filePath,"/");
            String fileName = CollUtil.getLast(list);
            oss.deleteObject(bucketName,fileName);
        } finally {
            oss.shutdown();
        }
    }
}
