package com.gsoft.projectManager.appuser.login;
import java.util.Date;

import com.gsoft.projectManager.appuser.AppUserService;
import com.gsoft.projectManager.appuser.UserPrincipal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
    public Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${app.jwtSecret}")
    private String jwtSecretKey;
    
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Autowired
    private AppUserService appUserService;

    public String generateJwtToken(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) appUserService.loadUserByUsername((String) authentication.getPrincipal());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                    .setSubject(Long.toString(userPrincipal.getId()))
                    .setIssuedAt(new Date())
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                    .compact();
    }

    public Long getUserIdFromJwtToken(String token){
        Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecretKey)
                        .parseClaimsJws(token)
                        .getBody();
        
        return Long.valueOf(claims.getSubject());
    }

    public boolean validateToken(String token){
        LOGGER.info("Validating token: ", token);
        try{
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException ex) {
			LOGGER.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			LOGGER.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			LOGGER.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			LOGGER.error("JWT claims string is empty");
		}
        return false;
    }
}
