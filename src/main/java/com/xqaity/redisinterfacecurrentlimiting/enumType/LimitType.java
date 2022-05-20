package com.xqaity.redisinterfacecurrentlimiting.enumType;

/**
 * @author Created by lenovo
 * @date 2022/5/20 11:59
 */
public enum LimitType {
	/**
	 * 默认策略全局限流
	 */
	DEFAULT,
	/**
	 * 根据请求者IP进行限流
	 */
	IP
}
