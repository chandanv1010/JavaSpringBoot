package chandanv.local.chandanv.modules.users.requests;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
public class RefreshTokenRequest {
    

    @NotBlank(message = "RefreshToken không được để trống")
    private String refreshToken;


}
