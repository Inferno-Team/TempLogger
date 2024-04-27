package site.inferno_team.TempLogger.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String PRIVATE_KEY = "78b546a577f2a14529a6d2f2e71627e0dfd8764496c0f40d010597ea65a00adf";

    public String extractUsername(String token) throws ExpiredJwtException {
        return extractClaims(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws ExpiredJwtException {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                &&
                !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) throws ExpiredJwtException {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws ExpiredJwtException {
        return extractClaims(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims,
            UserDetails userDetails) {
        Date now = new Date(System.currentTimeMillis());
        Date afterDay = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(afterDay);
        calendar.add(Calendar.MONTH, 3); // adding 3 months.
        afterDay = calendar.getTime();
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(afterDay)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(PRIVATE_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
