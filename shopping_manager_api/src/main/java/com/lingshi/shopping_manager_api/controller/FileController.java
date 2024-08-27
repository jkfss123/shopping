package com.lingshi.shopping_manager_api.controller;

import com.lingshi.shopping_common.service.result.BaseResult;
import com.lingshi.shopping_common.service.IFileService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {


    @DubboReference
    private IFileService fileService;

    @PostMapping("/uploadImage")
    public BaseResult uploadImage(MultipartFile file) throws IOException{
        String filePath = fileService.uploadImage(file.getBytes(),file.getOriginalFilename());
        return BaseResult.success(filePath);
    }
}
