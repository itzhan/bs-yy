package com.controller;

import com.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.alibaba.fastjson2.*;
import java.nio.charset.StandardCharsets;

/**
 * AI对话（OpenAI兼容接口）
 */
@RestController
@RequestMapping("/ai")
public class AIController {

    @Value("${ai.enabled:true}")
    private boolean enabled;
    @Value("${ai.base-url:https://openrouter.ai/api/v1}")
    private String baseUrl;
    @Value("${ai.api-key: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}")
    private String apiKey;
    @Value("${ai.model:deepseek/deepseek-chat-v3.1:free}")
    private String model;

    /**
     * Chat Completions（无需登录）
     * 请求体：{ "ask": "..." }
     */
    @RequestMapping("/chat")
    public R chat(@RequestBody String body) {
        if (!enabled) {
            return R.error("AI未启用");
        }
        try {
            JSONObject req = JSON.parseObject(body);
            String ask = String.valueOf(req.getOrDefault("ask", ""));
            if (ask == null || ask.isBlank()) {
                return R.error("缺少ask");
            }
            String url = baseUrl;
            if (!url.endsWith("/")) url += "/";
            url += "chat/completions";

            JSONObject payload = new JSONObject();
            payload.put("model", model);
            JSONArray messages = new JSONArray();
            JSONObject sys = new JSONObject();
            sys.put("role", "system");
            sys.put("content", "You are a helpful assistant.");
            messages.add(sys);
            JSONObject user = new JSONObject();
            user.put("role", "user");
            user.put("content", ask);
            messages.add(user);
            payload.put("messages", messages);
            payload.put("stream", false);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toJSONString(), StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return R.error("AI接口调用失败:" + response.statusCode());
            }
            JSONObject resp = JSON.parseObject(response.body());
            String reply = null;
            try {
                reply = resp.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            } catch (Exception ignored) {}
            if (reply == null) reply = "抱歉，我没有理解您的问题。";
            return R.ok().put("data", reply);
        } catch (Exception e) {
            return R.error("AI调用异常:" + e.getMessage());
        }
    }
}

