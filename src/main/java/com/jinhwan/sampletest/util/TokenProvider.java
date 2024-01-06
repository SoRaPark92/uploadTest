package com.jinhwan.sampletest.util;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider implements InitializingBean {
	
	private Logger logger = LoggerFactory.getLogger(TokenProvider.class);
	private static final String AUTHORITIES_KEY = "authority";
    private final long tokenValidityInMilliseconds;
    private Key key;
    
	private final String secretKey = "JinHwanTestApiSampleTest";
    
    public TokenProvider() { 
		this.tokenValidityInMilliseconds = 1000 * 60 * 60;	// 60분
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
    	//길이가 짧으면 토큰생성시 암호화 알고리즘을 사용할 수 없음
        byte[] keyBytes = HashUtil.sha256(HashUtil.md5(secretKey)).getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**token 생성 algorithm */
    public String createToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**인증 정보 조회 */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
        Collection<? extends GrantedAuthority> authorities = 
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principal =new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**token 유효성 검증 */
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
        }catch(ExpiredJwtException e){
        	logger.info("만료된 JWT 토큰입니다.");
        }catch(UnsupportedJwtException e){
        	logger.info("지원하지 않는 JWT 토큰입니다.");
        }catch(IllegalArgumentException e){
        	logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
