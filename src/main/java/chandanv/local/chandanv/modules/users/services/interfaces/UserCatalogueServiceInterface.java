package chandanv.local.chandanv.modules.users.services.interfaces;

import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;
import chandanv.local.chandanv.services.interfaces.BaseServiceInterface;

public interface UserCatalogueServiceInterface extends BaseServiceInterface<UserCatalogue, StoreRequest, UpdateRequest> {
    
}
