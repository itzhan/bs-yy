package com.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 图片识别结果
 */
public class ImageRecognitionResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String keyword;
    private Double score;
    private Map<String, Object> raw;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Map<String, Object> getRaw() {
        return raw;
    }

    public void setRaw(Map<String, Object> raw) {
        this.raw = raw;
    }
}
