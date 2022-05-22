package com.xqaity.redisinterfacecurrentlimiting.component;

import com.microsoft.windowsazure.exception.ServiceException;
import com.xqaity.redisinterfacecurrentlimiting.annotation.RateLimiter;
import com.xqaity.redisinterfacecurrentlimiting.enumType.LimitType;
import com.xqaity.redisinterfacecurrentlimiting.utils.IpInfoUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @author Created by lenovo
 * @date 2022/5/20 14:14
 */
@Aspect
@Component
public class RateLimiterAspect {
	private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

	@Autowired(required=true)
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired(required=true)
	private RedisScript<Long> limitScript;

	@Before("@annotation(rateLimiter)") //前置通知, 在方法执行之前执行之前扫描注解
	public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable {
		String key = rateLimiter.key(); //key
		int time = rateLimiter.time();// 每各多少秒的步长
		int count = rateLimiter.count();//步长次数

		String combineKey = getCombineKey(rateLimiter, point); //如果有ip 拿到key获取一个组合的 key，所谓的组合的 key，就是在注解的 key 属性基础上，再加上方法的完整路径，如果是 IP 模式的话，就再加上 IP 地址。以 IP 模式为例，最终生成的 key 类似这样
		List<Object> keys = Collections.singletonList(combineKey); //变为keyList
		try {
			Long number = redisTemplate.execute(limitScript, keys, count, time); //执行lua脚本
			if (number==null || number.intValue() > count) {
				throw new ServiceException("访问过于频繁，请稍候再试");
			}
			log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), key);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("服务器限流异常，请稍候再试");
		}
	}

	public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
		StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
		if (rateLimiter.limitType() == LimitType.IP) {
			stringBuffer.append(IpInfoUtil.getIpAddr(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())).append("-");
		}
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		Class<?> targetClass = method.getDeclaringClass();
		stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
		return stringBuffer.toString();
	}
}
