package com.example.insurance.security;

import com.example.insurance.model.UserEntity;
import com.example.insurance.service.UserLoginService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWtUtil {
    @Autowired
    UserLoginService userLoginService;

    public String key ="Ihc2GHVPc6JS7P5VsdQJmoE5kJkInLq9";
    final SecretKey secret_Key= Keys.hmacShaKeyFor(key.getBytes());
    public Map genarateToken(UserDetails userDetails) throws Exception {
        String JWT_TOKEN= Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() +1000 *60 *60))
                .signWith(secret_Key,Jwts.SIG.HS256).compact();

        UserEntity userDet=userLoginService.getByUserName(userDetails.getUsername());
        Map responceMap = new HashMap<>();
        responceMap.put("token",JWT_TOKEN);
        responceMap.put("userName",userDet.getUserName());
        responceMap.put("userRole",userDet.getRole());
        responceMap.put("userId",userDet.getDocumentId());
        return  responceMap;
    }

    public boolean validateToken(String token, UserDetails userDetails){
       return extractUserName(token).equals(userDetails.getUsername());
    }

    public String extractUserName(String token) {
        return Jwts.parser().verifyWith(secret_Key).build().parseSignedClaims(token).getPayload().getSubject();
    }

}
