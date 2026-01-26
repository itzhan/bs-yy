package com.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import com.annotation.IgnoreAuth;
import com.config.BizException;
import com.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传下载
 */
@RestController
@RequestMapping("file")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    @IgnoreAuth
    public R upload(@RequestParam("file") MultipartFile file, String type) throws Exception {
        if (file.isEmpty()) {
            throw new BizException("上传文件不能为空");
        }
        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString();
        try {
            // 检查文件名是否包含非法字符
            if (fileName.contains("..")) {
                throw new RuntimeException("文件名包含非法路径序列: " + fileName);
            }
            Path fileStorageLocation = Paths.get(uploadPath).toAbsolutePath().normalize();
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception ex) {
                throw new RuntimeException("无法创建文件存储目录", ex);
            }
            // 复制文件到目标位置
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return R.ok().put("file", "/upload/" + fileName);
        } catch (IOException ex) {
            throw new RuntimeException("无法存储文件: " + fileName, ex);
        }

    }

    /**
     * 下载文件
     */
    @IgnoreAuth
    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String fileName) {
        try {
            // 检查文件名是否包含非法字符
            if (fileName.contains("..")) {
                throw new RuntimeException("文件名包含非法路径序列: " + fileName);
            }

            Path fileStorageLocation = Paths.get(uploadPath).toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(fileName).normalize();

            // 确保文件路径在允许的目录内
            if (!filePath.startsWith(fileStorageLocation)) {
                throw new RuntimeException("文件路径不在允许的目录内: " + fileName);
            }

            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                byte[] fileContent = Files.readAllBytes(filePath);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileName);
                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            } else {
                log.warn("请求的文件不存在: {}", fileName);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            log.error("下载文件时发生错误: {}", fileName, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            log.error("文件下载安全检查失败: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
