package chandanv.local.chandanv.modules.users.services.interfaces;
import chandanv.local.chandanv.modules.users.requests.LoginRequest;

public interface UserServiceInterface {
    
    Object authenticate(LoginRequest request);

}
