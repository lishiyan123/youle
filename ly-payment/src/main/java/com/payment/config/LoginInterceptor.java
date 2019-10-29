package com.payment.config;

import com.shop.bean.User;
import com.shop.common.util.CookieUtils;
import com.shop.common.util.JsonUtils;
import com.shop.common.util.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bystander
 * @date 2018/10/3
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    StringRedisTemplate redisTemplate;


    //定义一个线程域，存放登录的对象
    private static final ThreadLocal<UserInfo> t1 = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //查询Token
        String token = CookieUtils.getCookieValue(request, "LY_TOKEN");
        if (StringUtils.isBlank(token)) {
            //用户未登录,返回401，拦截
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        //用户已登录，获取用户信息
        try {
            String userString = redisTemplate.opsForValue().get(token);
            User user = JsonUtils.toBean(userString, User.class);
            UserInfo userInfo = new UserInfo(user.getId(),user.getUsername());//JwtUtils.getUserInfo(props.getPublicKey(), token);
            //放入线程域中
            t1.set(userInfo);
            return true;
        } catch (Exception e) {
            //抛出异常，未登录
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //过滤器完成后，从线程域中删除用户信息
        t1.remove();
    }

    /**
     * 获取登陆用户
     * @return
     */
    public static UserInfo getLoginUser() {
        return t1.get();
    }
}
