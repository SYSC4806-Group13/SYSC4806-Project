package com.SYSC4806_Group13.SYSC4806_Project.Security;

import com.SYSC4806_Group13.SYSC4806_Project.Config.AppConfig;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final AppConfig appConfig;

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appConfig.getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject((userPrincipal.getId().toString() + "|" + userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appConfig.getTokenSecret())
                .compact();
    }

    /**
     * The subject is stored as id|email therefore split by |
     * 0 index is id and 1 index is email
     * @param token the jwt token
     * @return
     */
    public String[] getUserIdAndEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appConfig.getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        int index =  claims.getSubject().indexOf("|");

        String[] userIdAndEmail = {claims.getSubject().substring(0,index),
                claims.getSubject().substring(index+1,claims.getSubject().length())};

        return userIdAndEmail;
    }

    public boolean validateToken(String authToken) throws SignatureException {
        try {
            Jwts.parser().setSigningKey(appConfig.getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

}