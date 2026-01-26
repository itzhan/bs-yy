package com.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.config.BaiduImageProperties;
import com.config.BizException;
import com.dto.ImageRecognitionResult;
import com.service.ImageRecognitionService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 百度图像识别实现
 */
@Service("imageRecognitionService")
public class ImageRecognitionServiceImpl implements ImageRecognitionService {

    private static final Logger log = LoggerFactory.getLogger(ImageRecognitionServiceImpl.class);
    private static final String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";

    private final BaiduImageProperties properties;

    @Value("${upload.path}")
    private String uploadPath;

    private final Object tokenLock = new Object();
    private volatile String cachedToken;
    private volatile long tokenExpireAt;

    public ImageRecognitionServiceImpl(BaiduImageProperties properties) {
        this.properties = properties;
    }

    @Override
    public ImageRecognitionResult recognizeFromPath(String imagePath) {
        ensureConfigReady();
        Path filePath = resolveImagePath(imagePath);
        byte[] imageBytes = readImageBytes(filePath);
        String token = getAccessToken();
        JSONObject response = invokeRecognitionApi(token, imageBytes);
        JSONObject best = pickBestResult(response);
        if (best == null) {
            throw new BizException("未获取到识别结果");
        }
        ImageRecognitionResult result = new ImageRecognitionResult();
        result.setKeyword(best.getString("keyword"));
        result.setScore(best.getDouble("score"));
        result.setRaw(new HashMap<>(best));
        log.info("图片识别完成，关键字={}, score={}", result.getKeyword(), result.getScore());
        return result;
    }

    private void ensureConfigReady() {
        if (!properties.isEnabled()) {
            throw new BizException("图片识别功能未开启，请先配置 baidu.image.enabled=true");
        }
        if (StrUtil.isBlank(properties.getApiKey()) || StrUtil.isBlank(properties.getSecretKey())) {
            throw new BizException("未配置百度图像识别 API Key/Secret Key");
        }
    }

    private Path resolveImagePath(String imagePath) {
        if (StrUtil.isBlank(imagePath)) {
            throw new BizException("图片路径不能为空");
        }
        String normalized = imagePath.trim();
        int queryIndex = normalized.indexOf('?');
        if (queryIndex >= 0) {
            normalized = normalized.substring(0, queryIndex);
        }
        int uploadIndex = normalized.indexOf("/upload/");
        if (uploadIndex >= 0) {
            normalized = normalized.substring(uploadIndex + 8);
        }
        normalized = normalized.replace("\\", "/");
        normalized = StrUtil.removePrefix(normalized, "/");
        normalized = StrUtil.removePrefix(normalized, "upload/");
        if (normalized.startsWith("http") || normalized.contains(":")) {
            throw new BizException("仅支持识别上传目录下的图片");
        }
        if (normalized.contains("..")) {
            throw new BizException("非法的图片路径");
        }
        Path root = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path target = root.resolve(normalized).normalize();
        if (!target.startsWith(root)) {
            throw new BizException("非法的图片路径");
        }
        if (!Files.exists(target)) {
            throw new BizException("图片不存在");
        }
        return target;
    }

    private byte[] readImageBytes(Path filePath) {
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("读取图片失败: {}", filePath, e);
            throw new BizException("读取图片失败");
        }
    }

    private JSONObject invokeRecognitionApi(String token, byte[] imageBytes) {
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        StringBuilder body = new StringBuilder();
        body.append("image=")
            .append(URLEncoder.encode(base64, StandardCharsets.UTF_8));
        if (properties.getBaikeNum() > 0) {
            body.append("&baike_num=").append(properties.getBaikeNum());
        }
        HttpResponse response = HttpRequest.post(buildEndpoint(token))
            .setConnectionTimeout(properties.getConnectTimeout())
            .setReadTimeout(properties.getReadTimeout())
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(body.toString())
            .execute();
        if (response == null || StrUtil.isBlank(response.body())) {
            throw new BizException("调用百度图像识别失败");
        }
        JSONObject json = JSON.parseObject(response.body());
        if (json.containsKey("error_code")) {
            String errorMsg = json.getString("error_msg");
            log.warn("百度图像识别失败: {}", response.body());
            throw new BizException("百度图像识别失败: " + errorMsg);
        }
        return json;
    }

    private JSONObject pickBestResult(JSONObject response) {
        JSONArray array = response.getJSONArray("result");
        if (array == null || array.isEmpty()) {
            return null;
        }
        JSONObject best = null;
        double bestScore = -1d;
        for (int i = 0; i < array.size(); i++) {
            JSONObject current = array.getJSONObject(i);
            double score = current.getDoubleValue("score");
            if (best == null || score > bestScore) {
                best = current;
                bestScore = score;
            }
        }
        return best;
    }

    private String getAccessToken() {
        long now = System.currentTimeMillis();
        if (cachedToken != null && now < tokenExpireAt - 60_000) {
            return cachedToken;
        }
        synchronized (tokenLock) {
            if (cachedToken != null && now < tokenExpireAt - 60_000) {
                return cachedToken;
            }
            HttpResponse response = HttpRequest.post(TOKEN_URL)
                .form(Map.of(
                    "grant_type", "client_credentials",
                    "client_id", properties.getApiKey(),
                    "client_secret", properties.getSecretKey()
                ))
                .setConnectionTimeout(properties.getConnectTimeout())
                .setReadTimeout(properties.getReadTimeout())
                .execute();
            if (response == null || StrUtil.isBlank(response.body())) {
                throw new BizException("获取百度 accessToken 失败");
            }
            JSONObject json = JSON.parseObject(response.body());
            if (json.containsKey("error")) {
                throw new BizException("获取百度 accessToken 失败: " + json.getString("error_description"));
            }
            cachedToken = json.getString("access_token");
            int expiresIn = json.getIntValue("expires_in");
            tokenExpireAt = System.currentTimeMillis() + expiresIn * 1000L;
            return cachedToken;
        }
    }

    private String buildEndpoint(String token) {
        String endpoint = StrUtil.blankToDefault(properties.getEndpoint(), "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general");
        if (endpoint.contains("?")) {
            return endpoint + "&access_token=" + token;
        }
        return endpoint + "?access_token=" + token;
    }
}
