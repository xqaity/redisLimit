package com.xqaity.redisinterfacecurrentlimiting.exception;

import com.microsoft.windowsazure.exception.ServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by lenovo
 * @date 2022/5/20 14:16
 */
@RestControllerAdvice
public class GlobalException {
	@ExceptionHandler(ServiceException.class)
	public Map<String,Object> serviceException(ServiceException e) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("status", 500);
		map.put("message", e.getMessage());
		return map;
	}
}

