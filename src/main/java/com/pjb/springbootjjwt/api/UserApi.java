package com.pjb.springbootjjwt.api;

import com.alibaba.fastjson.JSONObject;
import com.pjb.springbootjjwt.annotation.UserLoginToken;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.entity.UserTo;
import com.pjb.springbootjjwt.service.TokenService;
import com.pjb.springbootjjwt.service.UserService;
import com.pjb.springbootjjwt.util.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("api")
public class UserApi {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    //登录
    @PostMapping("/login")
    public Object login( @RequestBody UserTo userTo){
        JSONObject jsonObject = new JSONObject();
        try {
            User user = new User();
            user.setUsername(userTo.getUsername());
            user.setPassword(MD5.encryptByMD5(userTo.getPassword()));

            User userForBase = userService.findByUsername(user);
            if (userForBase == null) {
                jsonObject.put("message", "登录失败,用户不存在");
                return jsonObject;
            } else {
                if (!userForBase.getPassword().equals(user.getPassword())) {
                    jsonObject.put("message", "登录失败,密码错误");
                    return jsonObject;
                } else {
                    String token = tokenService.getToken(userForBase);
                    jsonObject.put("token", token);
                    jsonObject.put("user", userForBase);
                    return jsonObject;
                }
            }
        }catch (Exception e) {
            log.error(e.getMessage());
            jsonObject.put("error", e.getMessage());
            return jsonObject;
        }
    }

    @PostMapping("/add")
    public Object addUser(@RequestBody UserTo userTo) {
        JSONObject jsonObject = new JSONObject();

        try {
            User user = new User();
            user.setUsername(userTo.getUsername());
            user.setPassword(MD5.encryptByMD5(userTo.getPassword()));

            userService.insertUser(user);
            jsonObject.put("msg", "add for " + user.getUsername() + " successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            jsonObject.put("error", e.getMessage());
        }
        return jsonObject;
    }
    
    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(HttpServletRequest request){
        String token = request.getHeader("token");
        UserTo userTo = tokenService.parseToken(token);
        if (userTo != null)
            return userTo.getUsername() + " 已通过验证";
        else
            return "未知错误";
    }
}
