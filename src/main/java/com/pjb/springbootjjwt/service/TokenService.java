package com.pjb.springbootjjwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.entity.UserTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service("TokenService")
public class TokenService {
    public String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(user.getId().toString(), user.getUsername())// 将 userid 和 username 保存到 token 里面
                .sign(Algorithm.HMAC256(user.getPassword()));// 以 password 作为 token 的密钥
        return token;
    }

    public UserTo parseToken(String token) {
        UserTo userTo = new UserTo();
        List<String> audience = JWT.decode(token).getAudience();
        try {
            userTo.setId(Integer.parseInt(audience.get(0)));
            userTo.setUsername(audience.get(1));
            log.info("current login user: " + userTo.getUsername());
            return userTo;
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
