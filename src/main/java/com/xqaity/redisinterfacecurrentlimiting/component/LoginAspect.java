package com.xqaity.redisinterfacecurrentlimiting.component;

import com.xqaity.redisinterfacecurrentlimiting.annotation.LoginNoValidator;
import com.xqaity.redisinterfacecurrentlimiting.utils.LoginUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author Created by lenovo
 * @date 2022/5/22 19:18
 */
@Component
@Order(1) // 设置优先级最高
@Slf4j
@Aspect
public class LoginAspect {
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Value(value = "${cjhx.tokenExpTime}")
	private String tokenExpTime;

//	@Autowired
//	private LoginService loginService;
/**
*  切点，所有RestController类注解的方法
*  拦截类或者是方法上标注注解的方法
*/

	@Pointcut(value = "@within(org.springframework.web.bind.annotation.RestController)")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object before(ProceedingJoinPoint joinpoint) throws Throwable {
		// 获取方法方法上的LoginValidator注解
		MethodSignature methodSignature = (MethodSignature) joinpoint.getSignature();
		Method method = methodSignature.getMethod();
		LoginNoValidator loginNoValidator = method.getAnnotation(LoginNoValidator.class);
		// 如果有，则不校验
		if (loginNoValidator != null) {
			return joinpoint.proceed(joinpoint.getArgs());
		}
		// 正常校验 获取request和response
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		if (requestAttributes == null || requestAttributes.getResponse() == null) {
		// 如果不是从前端过来的，没有request，则直接放行
			return joinpoint.proceed(joinpoint.getArgs());
		}
		HttpServletRequest request = requestAttributes.getRequest();
		HttpServletResponse response = requestAttributes.getResponse();
		// 获取token
		String token = request.getHeader("token");
		if (StringUtils.isBlank(token)) {
			returnNoLogin(response);
			return null;
		}
		//从redis获取用户token使用了UUID的方式，在redis中以uuid作为key，用户信息作为value存储在redis中
		Object user = redisTemplate.opsForValue().get(token);
		// FIXME token校验、token续期等
		//简单续期可以参考
		// 续期token
//redisTemplate.expire(key,loginProperties.getTokenExpTime(),TimeUnit.MINUTES);
		try {
			LoginUserUtil.put(user);
			return joinpoint.proceed(joinpoint.getArgs());
		}finally {
			// 保证ThreadLocal对象 必须删除，防止内存泄漏
			LoginUserUtil.remove();
		}
	}
//
//	/**
//	 *       * 返回未登录的错误信息
//	 *       * @param response ServletResponse
//	 *      
//	 */
//
	private void returnNoLogin(HttpServletResponse response) {

		log.info("用户登录校验失败");
		PrintWriter writer = null;
		try {
			response.setContentType("application/json;charset=UTF-8");
			writer = response.getWriter();
//			BaseResponse baseResponse = BaseResponse.fail(ErrorCode.LOGIN);
//			writer.write(JsonUtil.obj2String(baseResponse));
			writer.write("{" +
					"json"+
					"}");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			writer.close();
		}
	}

}

