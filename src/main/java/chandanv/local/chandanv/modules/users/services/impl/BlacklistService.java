package chandanv.local.chandanv.modules.users.services.impl;

import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import chandanv.local.chandanv.modules.users.entities.BlacklistedToken;
import chandanv.local.chandanv.modules.users.repositories.BlacklistedTokenRepository;
import chandanv.local.chandanv.modules.users.requests.BlacklistTokenRequest;
import chandanv.local.chandanv.resources.ApiResource;
import chandanv.local.chandanv.services.JwtService;
import io.jsonwebtoken.Claims;



@Service
public class BlacklistService {

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public Object create(BlacklistTokenRequest request){
        if(blacklistedTokenRepository.existsByToken(request.getToken())){
            return ApiResource.error("TOKEN_ERROR", "Token đã tồn tại", HttpStatus.BAD_REQUEST);
        }
        Claims claims = jwtService.getAllClaimsFromToken(request.getToken());
        Long userId = Long.valueOf(claims.getSubject());

        Date expiryDate = claims.getExpiration();
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(request.getToken());
        blacklistedToken.setUserId(userId);
        blacklistedToken.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        blacklistedTokenRepository.save(blacklistedToken);

        logger.info("Thêm token vào danh sách blacklist thành công");

        return ApiResource.message("Thêm token vào blacklist thành công", HttpStatus.OK);
    }
}
