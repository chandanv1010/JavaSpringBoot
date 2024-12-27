package chandanv.local.chandanv.modules.users.mappers;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import chandanv.local.chandanv.annotations.BaseMapperAnnotation;
import chandanv.local.chandanv.mappers.BaseMapper;
import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.UserCatalogueResource;


@Mapper(componentModel = "spring")
public interface  UserCatalogueMapper extends BaseMapper<UserCatalogue, UserCatalogueResource, StoreRequest, UpdateRequest> {
    
   
    @Override
    @BaseMapperAnnotation
    @Mapping(target = "permissions", ignore= true)
    @Mapping(target = "users", ignore= true)
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    UserCatalogue toEntity(StoreRequest createRequest);

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "permissions", ignore= true)
    @Mapping(target = "users", ignore= true)
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateRequest UpdateRequest, @MappingTarget UserCatalogue entity);
        
}
