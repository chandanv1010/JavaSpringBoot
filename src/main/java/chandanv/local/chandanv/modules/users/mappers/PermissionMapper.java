package chandanv.local.chandanv.modules.users.mappers;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import chandanv.local.chandanv.annotations.BaseMapperAnnotation;
import chandanv.local.chandanv.mappers.BaseMapper;
import chandanv.local.chandanv.modules.users.entities.Permission;
import chandanv.local.chandanv.modules.users.requests.Permission.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.Permission.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.PermissionResource;


@Mapper(componentModel = "spring")
public interface  PermissionMapper extends BaseMapper<Permission, PermissionResource, StoreRequest, UpdateRequest> {
    
    @Override
    @BaseMapperAnnotation
    @Mapping(target = "userCatalogues", ignore= true)
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    Permission toEntity(StoreRequest createRequest);


    @Override
    @Mapping(target = "userCatalogues", ignore = true) 
    @BaseMapperAnnotation
    void updateEntityFromRequest(UpdateRequest updateRequest, @MappingTarget Permission entity);

}
