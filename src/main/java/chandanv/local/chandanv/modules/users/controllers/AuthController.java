package chandanv.local.chandanv.modules.users.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chandanv.local.chandanv.modules.users.entities.RefreshToken;
import chandanv.local.chandanv.modules.users.repositories.RefreshTokenRepository;
import chandanv.local.chandanv.modules.users.requests.BlacklistTokenRequest;
import chandanv.local.chandanv.modules.users.requests.LoginRequest;
import chandanv.local.chandanv.modules.users.requests.RefreshTokenRequest;
import chandanv.local.chandanv.modules.users.resources.LoginResource;
import chandanv.local.chandanv.modules.users.resources.RefreshTokenResource;
import chandanv.local.chandanv.modules.users.services.impl.BlacklistService;
import chandanv.local.chandanv.modules.users.services.interfaces.UserServiceInterface;
import chandanv.local.chandanv.resources.ApiResource;
import chandanv.local.chandanv.services.JwtService;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final UserServiceInterface userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private  BlacklistService blacklistService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
   
    public AuthController(
        UserServiceInterface userService
    ){
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        Object result = userService.authenticate(request);

        if(result instanceof LoginResource loginResource){
            ApiResource<LoginResource> response = ApiResource.ok(loginResource, "SUCCESS");
            return ResponseEntity.ok(response);
        }

        if(result instanceof  ApiResource errorResource){
            return ResponseEntity.unprocessableEntity().body(errorResource);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Network Error");

    }

    @PostMapping("blacklisted_tokens")
    public ResponseEntity<?> addTokenToBlacklist(@Valid @RequestBody BlacklistTokenRequest request){
        try {

            logger.info(request.getToken());
            Object result = blacklistService.create(request);
            return ResponseEntity.ok(result); 

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResource.message("Network Error!", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("loggout")
    public ResponseEntity<?> loggout(@RequestHeader("Authorization") String bearerToken ){
        try {

            String token = bearerToken.substring(7);
            BlacklistTokenRequest request = new BlacklistTokenRequest();
            request.setToken(token);
            blacklistService.create(request);

            ApiResource<Void> response = ApiResource.<Void>builder()
                .success(true)
                .message("Đăng xuất thành công!")
                .status(HttpStatus.OK)
                .build();


            return ResponseEntity.ok(response); 

            
        } catch (Exception e) {

            ApiResource<Void> errorResponse = ApiResource.<Void>builder()
                .success(false)
                .message("Network Error!")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@Valid  @RequestBody RefreshTokenRequest request){
        
        String refreshToken = request.getRefreshToken();

        if(!jwtService.isRefreshTokenValid(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResource.message("Refresh Token không hợp lệ", HttpStatus.UNAUTHORIZED));
        }

        Optional<RefreshToken> dbRefreshTokenOptional = refreshTokenRepository.findByRefreshToken(refreshToken);

        if(dbRefreshTokenOptional.isPresent()){
            RefreshToken dBRefreshToken = dbRefreshTokenOptional.get();
            Long userId = dBRefreshToken.getUserId();
            String email = dBRefreshToken.getUser().getEmail();
            String newToken = jwtService.generateToken(userId, email, null);
            String newRefreshToken = jwtService.generateRefreshToken(userId, email);

            RefreshTokenResource refreshTokenResource = new RefreshTokenResource(newToken, newRefreshToken);

            ApiResource<RefreshTokenResource> response = ApiResource.ok(refreshTokenResource, "Refresh token thành công");

            return ResponseEntity.ok(response); 
        }

        return ResponseEntity.internalServerError().body(ApiResource.message("Network Error!", HttpStatus.INTERNAL_SERVER_ERROR));
    }
 
}

