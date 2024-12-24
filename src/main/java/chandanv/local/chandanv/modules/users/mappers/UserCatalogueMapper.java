package chandanv.local.chandanv.modules.users.mappers;
import org.mapstruct.Mapper;

import chandanv.local.chandanv.mappers.BaseMapper;
import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.UserCatalogueResource;


@Mapper(componentModel = "spring")
public interface  UserCatalogueMapper extends BaseMapper<UserCatalogue, UserCatalogueResource, StoreRequest, UpdateRequest> {
    
        
}
