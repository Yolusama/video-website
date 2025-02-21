package VideoServer.Functional;

import VideoServer.Common.Constants;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtGenerator {
    private JwtGenerator(){}
    public static String Generate(String secretKey,String userId,Long expire,String issuser,String audience)
    {
        Long nowTicks=new Date().getTime();
        Date exp = new Date(nowTicks+expire);
        Map<String,Object> claims=new HashMap<>();
        claims.put(Constants.JwtUserClaim,userId);
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setAudience(audience)
                .setIssuer(issuser)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(exp);
        return builder.compact();
    }
}
