package chandanv.local.chandanv.modules.users.mappers;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import chandanv.local.chandanv.annotations.BaseMapperAnnotation;
import chandanv.local.chandanv.mappers.BaseMapper;
import chandanv.local.chandanv.modules.users.entities.User;
import chandanv.local.chandanv.modules.users.requests.User.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.User.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.UserResource;


@Mapper(componentModel = "spring")
public interface  UserMapper extends BaseMapper<User, UserResource, StoreRequest, UpdateRequest> {
    
   
    @Override
    @BaseMapperAnnotation
    @Mapping(target = "userCatalogues", ignore= true)
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    User toEntity(StoreRequest createRequest);

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "userCatalogues", ignore= true)
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateRequest UpdateRequest, @MappingTarget User entity);
        
}
