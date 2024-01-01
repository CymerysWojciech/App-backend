package pl.budowniczowie.appbackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pl.budowniczowie.appbackend.user.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;


    public String generateToken(
            User user
    ) {
        return buildToken(user);
    }
    private String buildToken(
            User user
    ) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withIssuer("Eminem")
                .withSubject(user.getUsername())
                .withClaim("firstname", user.getFirstname())
                .withClaim("lastname", user.getLastname())
                .withClaim("email", user.getEmail())
                .withClaim("avatar", user.getPathAvatar())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }
    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {

        JWTVerifier verifier =  JWT.require(Algorithm.HMAC256(secretKey))
                        .build();
        DecodedJWT jwt = verifier.verify(token);
        String[] rolesArray = jwt.getClaim("roles").asArray(String.class);
        List<SimpleGrantedAuthority> collectRoles = Stream.of(rolesArray).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return  new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, collectRoles);

    }

    boolean isTokenValid(String jwt) {
        return ( !isTokenExpired(jwt));
    }
    private boolean isTokenExpired(String jwt) {
        Date expiration = extractExpiration(jwt);
        Date now = new Date();
        return expiration.before(now);
    }

    private Date extractExpiration(String jwt) {
        return JWT.decode(jwt).getExpiresAt();
    }
}
