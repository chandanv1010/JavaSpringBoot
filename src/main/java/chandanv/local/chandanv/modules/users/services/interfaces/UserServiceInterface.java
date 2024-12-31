package chandanv.local.chandanv.modules.users.services.interfaces;
import chandanv.local.chandanv.modules.users.entities.User;
import chandanv.local.chandanv.modules.users.requests.LoginRequest;
import chandanv.local.chandanv.modules.users.requests.User.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.User.UpdateRequest;
import chandanv.local.chandanv.services.interfaces.BaseServiceInterface;

public interface UserServiceInterface extends BaseServiceInterface<User, StoreRequest, UpdateRequest> {
    
    Object authenticate(LoginRequest request);

}
