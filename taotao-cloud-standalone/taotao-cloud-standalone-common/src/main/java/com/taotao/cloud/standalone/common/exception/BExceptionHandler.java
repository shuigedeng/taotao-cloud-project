package com.taotao.cloud.standalone.common.exception;

import com.taotao.cloud.standalone.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;

/**
 * @Classname BExceptionHandler
 * @Description 自定义异常处理
 * @Author 李号东 lihaodongmail@163.com
 * @Date 2019-03-29 13:23
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class BExceptionHandler {


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(PreBaseException.class)
    public R handlerException(PreBaseException e) {
        return R.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error(404, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return R.error(300, "数据库中已存在该记录");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R handleAuthorizationException(AccessDeniedException e) {
        log.error(e.getMessage());
        return R.error(403, "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public R handleAccountExpiredException(AccountExpiredException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public R handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public R handlerSqlException(SQLException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }
    @ExceptionHandler(ValidateCodeException.class)
    public R handleValidateCodeException(ValidateCodeException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }

}
