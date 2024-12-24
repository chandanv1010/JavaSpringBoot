package chandanv.local.chandanv.mappers;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

public interface BaseMapper <E, R, C, U> {

    //Entity -> Resource
    R tResource(E entity);


    // List Entity --> List Resource
    List<R> toList(List<E> entities);

    // Page
    default Page<R> toResourcePage(Page<E> page){
        return page.map(this::tResource);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore= true)
    @Mapping(target = "updatedAt", ignore= true)
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    E toEntity(C createRequest);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore= true)
    @Mapping(target = "updatedAt", ignore= true)
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(U UpdateRequest, @MappingTarget E entity);

 
}
