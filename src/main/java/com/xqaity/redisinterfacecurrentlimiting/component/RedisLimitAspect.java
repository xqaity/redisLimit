//package com.xqaity.redisinterfacecurrentlimiting.component;
//
//import com.microsoft.windowsazure.exception.ServiceException;
//import com.xqaity.redisinterfacecurrentlimiting.annotation.RateLimiter;
//import com.xqaity.redisinterfacecurrentlimiting.enumType.LimitType;
//import com.xqaity.redisinterfacecurrentlimiting.utils.IpInfoUtil;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.annotation.Resource;
//import java.lang.reflect.Method;
//import java.util.Collections;
//import java.util.List;
//
///**
// * @author Created by lenovo
// * @date 2022/5/22 11:28
// */
//@Aspect
//@Component
//public class RedisLimitAspect {
//	@Resource(name = "lua1")
//	private DefaultRedisScript redisScript;
//	@Autowired
//	private RedisTemplate redisTemplate;
//
//	@Before("@annotation(rateLimiter)")
//	public void  doBefore(RateLimiter rateLimiter, JoinPoint joinPoint)  throws Throwable{
//		String key = rateLimiter.key(); // 唯一标识
//		int count = rateLimiter.count();// 限流数量
//		int time = rateLimiter.time();//过期时间
//		String keys = getKey(rateLimiter,joinPoint);
//		List<Object> listKey = Collections.singletonList(keys);
//		Long number = (Long)redisTemplate.execute(redisScript,listKey,count,time);
//			if(number == null || number>count){
//				throw new RuntimeException("访问过于频繁，请稍候再试");
//			}
//
//	}
//
//	public String getKey(RateLimiter rateLimiter, JoinPoint joinPoint){
//		StringBuilder stringBuilder =	new StringBuilder(rateLimiter.key());
//		if(rateLimiter.limitType()== LimitType.IP)
//		{
//			stringBuilder.append(IpInfoUtil.getIpAddr(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())).append("-");
//		}
//		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
//		Method method = methodSignature.getMethod();
//		Class<?> tClass = method.getDeclaringClass();
//		stringBuilder.append(tClass.getName()).append("-").append(method.getName());
//		return stringBuilder.toString();
//	}
//}
