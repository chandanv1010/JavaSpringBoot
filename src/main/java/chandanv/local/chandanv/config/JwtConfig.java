package chandanv.local.chandanv.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JwtConfig {
    

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.issuer}")
    private String issuer;


    @Value("${jwt.expirationRefreshToken}")
    private long refreshTokenExpirationTime;


    public String getSecretKey(){
        return secretKey;
    }

    public long getExpirationTime(){
        return expirationTime;
    }

    public String getIssuer(){
        return issuer;
    }

    public long getRefreshTokenExpirationTime(){
        return refreshTokenExpirationTime;
    }

}
