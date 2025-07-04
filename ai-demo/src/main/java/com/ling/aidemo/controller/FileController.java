package com.ling.aidemo.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Author: guoqing.ling@wfjt.com
 * Date: 2025/07/03  14:55
 * package_name: com.ling.aidemo.controller
 * classname : FileController
 */
@RestController
@RequestMapping("/file")
public class FileController {


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "文件为空";
        }
        String fileName = file.getOriginalFilename();
        File dest = new File("E:\\ordercode\\ai-demo\\" + fileName);
        try {
            file.transferTo(dest);
            return dest.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}