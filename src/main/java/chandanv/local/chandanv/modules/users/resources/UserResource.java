package chandanv.local.chandanv.modules.users.resources;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.util.Set;
import chandanv.local.chandanv.modules.users.entities.UserCatalogue;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResource {
    
    private final Long id;
    private final String email;
    private final String name;
    private final String phone;


    private Set<UserCatalogue> userCatalogues;
   

}
