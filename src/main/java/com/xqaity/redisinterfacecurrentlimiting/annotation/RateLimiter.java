package com.xqaity.redisinterfacecurrentlimiting.annotation;

import com.xqaity.redisinterfacecurrentlimiting.enumType.LimitType;

import java.lang.annotation.*;

/**
 * @author Created by lenovo
 * @date 2022/5/20 13:39
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
	/**
	 * 限流key
	 */
	String key() default "rate_limit:";

	/**
	 * 限流时间,单位秒
	 */
	int time() default 60;

	/**
	 * 限流次数
	 */
	int count() default 100;

	/**
	 * 限流类型
	 */
	LimitType limitType() default LimitType.DEFAULT;
}
