package com.user.service;

import com.shop.bean.User;
import com.shop.common.enums.ExceptionEnum;
import com.shop.common.exception.LyException;
import com.shop.common.util.CodecUtils;
import com.shop.common.util.JsonUtils;
import com.user.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.shop.common.util.NumberUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    static final String KEY_PREFIX = "user:code:phone:";



    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }
        return this.userMapper.selectCount(record) == 0;
    }

    /**
     * 生成验证码 往手机上发送
     * @param phone
     */
    public void sendVerifyCode(String phone) {
        /**
         * 生成6位验证码
         */
        String code = NumberUtils.generateCode(6);
        System.out.println("验证码为："+code);
        HashMap<String ,String> kvHashMap = new HashMap<>();
        kvHashMap.put("phone",phone);
        kvHashMap.put("code",code);


        try {
            /**
             * 将消息发送队列 发送至手机
             */

            amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",kvHashMap);

            /**
             * 添加到缓存
             */

            redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
        } catch (AmqpException e) {
            e.printStackTrace();
        }


    }

    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        // 从redis取出验证码
        String codeCache = redisTemplate.opsForValue().get(key);
        System.out.println("codeCache="+codeCache);
        if(!code.equals(codeCache)){
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        boolean boo = this.userMapper.insertSelective(user) == 1;
        // 如果注册成功，删除redis中的code
        if (boo) {
            try {
                this.redisTemplate.delete(key);
            } catch (Exception e) {
                System.out.println("删除缓存验证码失败，code：{}"+code+e);
            }
        }
        return boo;
    }

    public String queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);

        //首先根据用户名查询用户
        User user = userMapper.selectOne(record);

        if (user == null) {
            throw new LyException(ExceptionEnum.USER_NOT_EXIST);
        }

        //检验密码是否正确
        if (!StringUtils.equals(CodecUtils.md5Hex(password, user.getSalt()), user.getPassword())) {
            //密码不正确
            throw new LyException(ExceptionEnum.PASSWORD_NOT_MATCHING);
        }
        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(token, JsonUtils.toString(user),1800,TimeUnit.SECONDS);
        return token;
    }


    public User getUserByRedis(String token) {
        String userString = (String) redisTemplate.opsForValue().get(token);
        if(StringUtils.isBlank(userString)){
            return null;
        }
        User user = JsonUtils.toBean(userString, User.class);
        return JsonUtils.toBean(userString, User.class);

    }

    public String addUserByRedis(String token,User user){
        redisTemplate.opsForValue().set(token,JsonUtils.toString(user),1800,TimeUnit.SECONDS);
        return token;
    }
}
