package com.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图片识别请求
 */
public class ImageRecognitionRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
