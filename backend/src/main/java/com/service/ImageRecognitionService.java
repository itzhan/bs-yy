package com.service;

import com.dto.ImageRecognitionResult;

/**
 * 图片识别服务
 */
public interface ImageRecognitionService {

    /**
     * 根据上传后的相对路径执行识别
     * @param imagePath /upload/xxx 形式的路径
     * @return 识别结果
     */
    ImageRecognitionResult recognizeFromPath(String imagePath);
}
