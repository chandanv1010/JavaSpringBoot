package chandanv.local.chandanv.modules.users.requests;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
public class BlacklistTokenRequest {
    

    @NotBlank(message = "Token không được để trống")
    private String token;


}
