package com.xqaity.redisinterfacecurrentlimiting.controller;

import com.xqaity.redisinterfacecurrentlimiting.annotation.RateLimiter;
import com.xqaity.redisinterfacecurrentlimiting.enumType.LimitType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by lenovo
 * @date 2022/5/22 12:31
 */
@RestController
public class testController {

	@RateLimiter(key = "testKey",time = 5,count = 2,limitType = LimitType.IP)
	@RequestMapping("/test")
	public Map test(){

		Map map = new HashMap<>();
		map.put("ok","123");
		return map;
	}
}
