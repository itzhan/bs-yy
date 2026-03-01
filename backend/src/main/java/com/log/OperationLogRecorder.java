package com.log;

import com.entity.LogEntity;
import com.service.LogService;
import com.service.TokenService;
import com.entity.TokenEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 操作日志记录器
 *
 * 通过统一入口收集数据修改行为，避免在业务代码中散落日志逻辑。
 */
@Component
public class OperationLogRecorder {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogRecorder.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String LOG_TABLE = "log";
    private static final int DETAIL_MAX_LENGTH = 2000;

    private final LogService operationLogService;
    private final TokenService tokenService;

    @Autowired
    public OperationLogRecorder(LogService operationLogService, TokenService tokenService) {
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
    }

    /**
     * 记录一次业务操作
     *
     * @param tableName   业务表名
     * @param moduleLabel 模块名称
     * @param action      操作类型（新增/修改/删除）
     * @param payload     当前业务数据
     * @param request     HTTP请求
     */
    public void record(String tableName, String moduleLabel, String action, Object payload, HttpServletRequest request) {
        if (request == null || LOG_TABLE.equalsIgnoreCase(tableName)) {
            return;
        }
        try {
            TokenEntity tokenEntity = resolveTokenEntity(request);
            LogEntity entity = new LogEntity();
            entity.setTablename(tableName);
            entity.setModule(Optional.ofNullable(moduleLabel).filter(s -> !s.isBlank()).orElse(tableName));
            entity.setOperatetype(action);
            entity.setBusinessid(extractBusinessId(payload));
            entity.setOperatorid(resolveOperatorId(request, tokenEntity));
            entity.setOperatorname(resolveOperatorName(request, tokenEntity));
            entity.setRequesturl(Optional.ofNullable(request.getRequestURI()).orElse(""));
            entity.setRequestmethod(Optional.ofNullable(request.getMethod()).orElse(""));
            entity.setRequestip(resolveIp(request));
            entity.setDetail(buildDetail(payload));
            entity.setAddtime(new Date());
            operationLogService.save(entity);
        } catch (Exception e) {
            LOGGER.debug("记录操作日志失败: {}", e.getMessage());
        }
    }

    private Long extractBusinessId(Object payload) {
        if (payload == null) {
            return null;
        }
        try {
            Method method = payload.getClass().getMethod("getId");
            Object value = method.invoke(payload);
            return extractLong(value);
        } catch (Exception ignored) {
            return null;
        }
    }

    private TokenEntity resolveTokenEntity(HttpServletRequest request) {
        if (tokenService == null) {
            return null;
        }
        String token = request.getHeader("Token");
        if (token == null || token.isBlank()) {
            token = request.getHeader("token");
        }
        if ((token == null || token.isBlank()) && request.getSession(false) != null) {
            Object attr = request.getSession(false).getAttribute("token");
            if (attr != null) {
                token = String.valueOf(attr);
            }
        }
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            return tokenService.getTokenEntity(token);
        } catch (Exception e) {
            LOGGER.debug("解析Token失败: {}", e.getMessage());
            return null;
        }
    }

    private Long resolveOperatorId(HttpServletRequest request, TokenEntity tokenEntity) {
        Object sessionValue = request.getSession(false) != null ? request.getSession(false).getAttribute("userId") : null;
        Long sessionId = extractLong(sessionValue);
        if (sessionId != null) {
            return sessionId;
        }
        if (tokenEntity != null) {
            return extractLong(tokenEntity.getUserid());
        }
        return null;
    }

    private String resolveOperatorName(HttpServletRequest request, TokenEntity tokenEntity) {
        Object sessionValue = request.getSession(false) != null ? request.getSession(false).getAttribute("username") : null;
        String sessionName = extractString(sessionValue);
        if (sessionName != null && !sessionName.isBlank()) {
            return sessionName;
        }
        if (tokenEntity != null && tokenEntity.getUsername() != null) {
            return tokenEntity.getUsername();
        }
        return "系统";
    }

    private Long extractLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String extractString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String resolveIp(HttpServletRequest request) {
        List<String> headers = List.of(
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP"
        );
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
                int commaIndex = ip.indexOf(',');
                return commaIndex > 0 ? ip.substring(0, commaIndex).trim() : ip.trim();
            }
        }
        return Optional.ofNullable(request.getRemoteAddr()).orElse("");
    }

    private String buildDetail(Object payload) {
        if (payload == null) {
            return "";
        }
        try {
            String json = OBJECT_MAPPER.writeValueAsString(payload);
            return truncate(json);
        } catch (Exception e) {
            return truncate(String.valueOf(payload));
        }
    }

    private String truncate(String content) {
        if (content == null) {
            return "";
        }
        return content.length() <= DETAIL_MAX_LENGTH ? content : content.substring(0, DETAIL_MAX_LENGTH);
    }
}
