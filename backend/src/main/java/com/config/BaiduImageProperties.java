package com.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 百度图像识别配置
 */
@Component
@ConfigurationProperties(prefix = "baidu.image")
public class BaiduImageProperties {

    private boolean enabled = false;
    private String apiKey;
    private String secretKey;
    private String endpoint = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
    private int baikeNum = 0;
    private int connectTimeout = 6000;
    private int readTimeout = 8000;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getBaikeNum() {
        return baikeNum;
    }

    public void setBaikeNum(int baikeNum) {
        this.baikeNum = baikeNum;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
