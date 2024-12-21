package chandanv.local.chandanv.modules.users.services.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;

public interface UserCatalogueServiceInterface {
    
    UserCatalogue create(StoreRequest request);
    UserCatalogue update(Long id, UpdateRequest request);
    Page<UserCatalogue> paginate(Map<String, String[]> parameters);
    List<UserCatalogue> getAll(Map<String, String[]> parameters);

}
