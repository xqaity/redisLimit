package com.xqaity.redisinterfacecurrentlimiting.utils;

/**
 * @author Created by lenovo
 * @date 2022/5/22 19:40
 */

public class LoginUserUtil {

	/**
	 * 线程池变量
	 */

	private static final ThreadLocal<Object> LOGIN_USER = new ThreadLocal<>();

	private LoginUserUtil() {
	}

	public static Object get() {
		return LOGIN_USER.get();
	}

	public static void put(Object user) {
		LOGIN_USER.set(user);
	}

	public static void remove() {
		LOGIN_USER.remove();
	}

}

