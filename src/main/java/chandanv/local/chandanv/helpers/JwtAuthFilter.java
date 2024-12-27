package chandanv.local.chandanv.helpers;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import chandanv.local.chandanv.modules.users.services.impl.CustomUserDetailsService;
import chandanv.local.chandanv.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
    
    private final JwtService jwtService;
    private final CustomUserDetailsService CustomUserDetailsService;
    private final ObjectMapper objectMapper;


    @Override
    protected boolean shouldNotFilter(
        @NonNull HttpServletRequest request
    ){
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/login") || 
        path.startsWith("/api/v1/auth/refresh");
    }


    @Override
    public void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userId;

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                sendErrorResponse(response, 
                    request, 
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Xác thực không thành công", 
                    "Không tìm thấy Token."
                );
                // filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);

            

            if(!jwtService.isTokenFormatValid(jwt)){
                sendErrorResponse(response, 
                    request, 
                    HttpServletResponse.SC_UNAUTHORIZED, 
                    "Xác thực không thành công", 
                    "Token không đúng định dạng."
                );
                return;
            }

            if(jwtService.isTokenExpired(jwt)){
                sendErrorResponse(response, 
                    request, 
                    HttpServletResponse.SC_UNAUTHORIZED, 
                    "Xác thực không thành công", 
                    "Token đã hết hạn."
                );
                return;
            }

            if(!jwtService.isSignatureValid(jwt)){
                sendErrorResponse(response, 
                    request, 
                    HttpServletResponse.SC_UNAUTHORIZED, 
                    "Xác thực không thành công", 
                    "Chữ ký không hợp lệ."
                );
                return;
            }

            if(!jwtService.isIssuerToken(jwt)){
                sendErrorResponse(response, 
                    request, 
                    HttpServletResponse.SC_UNAUTHORIZED, 
                    "Xác thực không thành công", 
                    "Nguồn Token không hợp lệ."
                );
                return;
            }
            
           

            
            if(jwtService.isBlacklistedToken(jwt)){
                sendErrorResponse(response, 
                    request, 
                    HttpServletResponse.SC_UNAUTHORIZED, 
                    "Xác thực không thành công", 
                    "Token bị khóa."
                );
                return;
            }

            userId = jwtService.getUserIdFromJwt(jwt);
            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = CustomUserDetailsService.loadUserByUsername(userId); 

                final String emailFromToken = jwtService.getEmailFromJwt(jwt);
                if(!emailFromToken.equals(userDetails.getUsername())){
                    sendErrorResponse(response, 
                        request, 
                        HttpServletResponse.SC_UNAUTHORIZED, 
                        "Xác thực không thành công", 
                        "User Token không chính xác."
                    );
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Xác thực tài khoản thành công: " + userDetails.getUsername());
            }

            filterChain.doFilter(request, response);
            
        } catch (ServletException | IOException e) {
            sendErrorResponse(response, 
                request, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Network Error!", 
                e.getMessage()
            );
        }

    }

    private void sendErrorResponse(
        @NotNull HttpServletResponse response,
        @NotNull HttpServletRequest request,
        int statusCode,
        String error,
        String message
    ) throws IOException {

        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorResponse = new HashMap<>(); 

        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("status", statusCode);
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", request.getRequestURI());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }

    
}
