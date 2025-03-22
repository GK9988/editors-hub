package org.garvk.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.garvk.authservice.exception.AuthServiceException;
import org.garvk.authservice.exception.TokenValidationException;
import org.garvk.authservice.model.User;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtils {

    private String SECRET_KEY;

    public JwtUtils(){
        this.SECRET_KEY = generateSecurityKey();
    }

    private String generateSecurityKey(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGenerator.generateKey();
            System.out.println("Secret Key is: " + secretKey.toString());
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new AuthServiceException(e.getMessage(), e);
        }
    }

    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String aInUsername){
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(aInUsername)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .signWith(getKey()).compact();

    }

    public Claims extractAllClaims(String aInToken){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(aInToken).getPayload();
    }

    public String extractUsername(String aInToken){
        return extractClaim(aInToken, Claims::getSubject);
    }

    public Date extractExpiration(String aInToken){
        return extractClaim(aInToken, Claims::getExpiration);
    }

    public <T> T extractClaim(String aInToken, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(aInToken);
        return claimResolver.apply(claims);
    }

    public boolean isTokenExpired(String aInToken){
        return extractExpiration(aInToken).before(new Date());
    }

    public boolean validateToken(String aInToken, String aInUserName){

        try{
            Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(aInToken);
        } catch (Exception e){
            throw new TokenValidationException(e.getMessage());
        }

        String lUserName = extractUsername(aInToken);

        boolean userNameCheck = lUserName.equals(aInUserName);

        return userNameCheck && !isTokenExpired(aInToken);
    }

}
