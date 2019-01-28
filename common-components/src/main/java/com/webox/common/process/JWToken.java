package com.webox.common.process;

import java.util.Date;

import com.webox.common.model.Response;
import com.webox.common.utils.AppConsts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWToken {

    @Value("${encrypted.tokenSecret}")
    private String tokenSecret;

    @Value("${token.duration}")
    private int duration;

    public Response generateToken(String tokenSeed) {
        Response response = new Response();
        if (tokenSeed == null) {
            response.setAppInfo("Token seed required");

        } else {
            String jwtToken = Jwts.builder().claim("tokenSeed", tokenSeed).setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + this.duration))
                    .signWith(SignatureAlgorithm.HS256, this.tokenSecret).compact();
            response.setToken(jwtToken);
            response.setAppStatus(AppConsts.RETURN_TRUE);
        }
        return response;
    }
}