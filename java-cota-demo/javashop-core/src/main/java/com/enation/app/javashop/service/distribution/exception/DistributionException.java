package com.enation.app.javashop.service.distribution.exception;

import com.enation.app.javashop.framework.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * 分销异常类
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018-03-26 下午5:26
 */
public class DistributionException extends ServiceException {


    public DistributionException(String code, String message){

        super(code,message);
        this.statusCode= HttpStatus.INTERNAL_SERVER_ERROR;
        statusCode=HttpStatus.BAD_REQUEST;

    }

}
