package com.user.controller;

import com.shop.bean.User;
import com.shop.common.enums.ExceptionEnum;
import com.shop.common.exception.LyException;
import com.shop.common.util.CookieUtils;
import com.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable(value = "type") Integer type) {
        Boolean boo = this.userService.checkData(data, type);
        if (boo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(boo);
    }

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @PostMapping("send")
    public void sendVerifyCode(@RequestParam("phone") String phone) {
        userService.sendVerifyCode(phone);
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code){
        Boolean boo = userService.register(user,code);
        if(boo == null || !boo){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        String ly_token = CookieUtils.getCookieValue(request, "LY_TOKEN");
        System.out.println("login======================"+ly_token);

        if(ly_token != null){
            //验证token值是不是在redis当中
            User user = userService.getUserByRedis(ly_token);
            if(user != null){
                return ResponseEntity.ok().build();
            }
        }

        ly_token = userService.queryUser(username, password);
        if (StringUtils.isBlank(ly_token)) {
            throw new LyException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        //将Token写入cookie中
        CookieUtils.setCookie(request, response, "LY_TOKEN",
                ly_token, 1800, null, true);
        return ResponseEntity.ok().build();
    }

    /**
     * 验证用户信息
     *
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<User> verifyUser(@CookieValue("LY_TOKEN") String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        System.out.println("token="+token);
        try {
            //从Token中获取用户信息
            User user = userService.getUserByRedis(token);
            if(user==null){
                System.out.println("没有找到用户");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            User userInfo = new User();
            System.out.println(userInfo.getUsername()+userInfo.getId());
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            //重新添加到redis中
            userService.addUserByRedis(token,user);
            //将newToken写入cookie中
            CookieUtils.setCookie(request, response, "LY_TOKEN",
                    token, 1800, null, true);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            //Token无效
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }





}
