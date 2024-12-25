package chandanv.local.chandanv.modules.users.services.interfaces;

import chandanv.local.chandanv.modules.users.entities.Permission;
import chandanv.local.chandanv.modules.users.requests.Permission.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.Permission.UpdateRequest;

public interface PermissionServiceInterface extends BaseServiceInterface<Permission, StoreRequest, UpdateRequest> {
    
}
