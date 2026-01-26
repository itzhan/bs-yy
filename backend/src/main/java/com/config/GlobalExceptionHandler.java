package com.config;

import com.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public R bizExceptionHandler(HttpServletRequest req, BizException e){
        log.error("发生业务异常！原因是：{}",e.getMsg());
        return R.error(e.getCode(),e.getMsg());
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("发生空指针异常！原因是:",e);
        return R.error(400,"请求的数据格式不符!");
    }

    /**
     * 处理静态资源未找到异常
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseBody
    public R noResourceFoundExceptionHandler(HttpServletRequest req, NoResourceFoundException e){
        log.warn("静态资源未找到: {}", e.getResourcePath());
        return R.error(404,"请求的资源不存在!");
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, Exception e){
        log.error("未知异常！原因是:",e);
        return R.error(500,"服务器内部错误!");
    }
}
