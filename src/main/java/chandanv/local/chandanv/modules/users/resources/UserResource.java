package chandanv.local.chandanv.modules.users.resources;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResource {
    
    private final Long id;
    private final String email;
    private final String name;
    private final String phone;

   

}
