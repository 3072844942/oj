package org.oj.server.controller;

import lombok.extern.log4j.Log4j2;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.jni.JudgeJNIService;
import org.oj.server.vo.PageVO;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础接口
 *
 * @author march
 * @since 2023/5/31 上午10:45
 */
@Log4j2
@RestControllerAdvice
public abstract class BaseController {
    @ExceptionHandler(WarnException.class)
    public final Object webExchangeBindException(WarnException ex) {
        return warn(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(ErrorException.class)
    public final Object webExchangeErrorException(ErrorException ex) {
        return error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected final Object exception(Exception ex) {
        return error("程序内部错误: " + ex.getMessage());
    }

    protected final Object error(String msg) {
        return createResult(StatusCodeEnum.SYSTEM_ERROR.getCode(), msg, null);
    }

    protected final Object error(Integer code, String msg) {
        return createResult(code, msg, null);
    }

    protected final Object warn(String msg) {
        return createResult(StatusCodeEnum.FAIL.getCode(), msg, null);
    }

    protected final Object warn(Integer code, String msg) {
        return createResult(code, msg, null);
    }

    protected final Object ok(Object data, Long total) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("list", data);
        map.put("total", total);
        return createResult(StatusCodeEnum.SUCCESS.getCode(), null, map);
    }

    protected final Object ok(PageVO<?> data) {
        return ok(data.getList(), data.getTotal());
    }

    protected final Object ok(Object data) {
        return createResult(StatusCodeEnum.SUCCESS.getCode(), null, data);
    }

    protected final Object ok() {
        return createResult(StatusCodeEnum.SUCCESS.getCode(), null, null);
    }

    private Object createResult(int code, String msg, Object data) {
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", code);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }

    @InitBinder
    protected void dateBinder(WebDataBinder binder) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor editor = new CustomDateEditor(format, true);
        binder.registerCustomEditor(Date.class, editor);
    }
}
