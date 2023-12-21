package project.booker.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("{jwt.token.key}")
    private String secretKey;

    //보안을 위해 secretKey를 Base64로 인코딩하기
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //Jwt객체 생성(accessToken, refreshToken)
    public Jwt createJwt(Map<String, Object> claims){
        String accessToken = createToken(claims, getExpireDateAccessToken());
        String refreshToken = createToken(new HashMap<>(), getExpireDateRefreshToken());
        return Jwt.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //JWT 토큰에서 Claim 정보 추출하는 함수
    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //JWT 토큰 생성 함수
    public String createToken(Map<String, Object> claims, Date expireDate){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //AccessToken 만료 기간 설정(30분)
    private Date getExpireDateAccessToken() {
        long expireTime = 1000 * 60 * 30;
        return new Date(System.currentTimeMillis() + expireTime);
    }

    //RefreshToken 만료 기간 설정(24시간)
    private Date getExpireDateRefreshToken() {
        long expireTime = 1000 * 60 * 60 * 24;
        return null;
    }

}
