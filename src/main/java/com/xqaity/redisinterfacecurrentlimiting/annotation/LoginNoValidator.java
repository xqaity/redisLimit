package com.xqaity.redisinterfacecurrentlimiting.annotation;

/**
 * @author Created by lenovo
 * @date 2022/5/22 19:23
 */

import java.lang.annotation.*;

/**
 *   * 免登录接口注解，使用方法：在需要免登录的接口添加该注解
 *  
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginNoValidator {

}
