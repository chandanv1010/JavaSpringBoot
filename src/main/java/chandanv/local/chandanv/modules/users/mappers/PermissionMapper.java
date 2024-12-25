package chandanv.local.chandanv.modules.users.mappers;
import org.mapstruct.Mapper;

import chandanv.local.chandanv.mappers.BaseMapper;
import chandanv.local.chandanv.modules.users.entities.Permission;
import chandanv.local.chandanv.modules.users.requests.Permission.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.Permission.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.PermissionResource;


@Mapper(componentModel = "spring")
public interface  PermissionMapper extends BaseMapper<Permission, PermissionResource, StoreRequest, UpdateRequest> {
    
        
}
