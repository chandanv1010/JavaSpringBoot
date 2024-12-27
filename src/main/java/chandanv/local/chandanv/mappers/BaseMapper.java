package chandanv.local.chandanv.mappers;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import chandanv.local.chandanv.annotations.BaseMapperAnnotation;



public interface BaseMapper <E, R, C, U> {


    //Entity -> Resource
    R tResource(E entity);


    // List Entity --> List Resource
    List<R> toList(List<E> entities);

    // Page
    default Page<R> toResourcePage(Page<E> page){
        return page.map(this::tResource);
    }

    @BaseMapperAnnotation
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    E toEntity(C createRequest);


    @BaseMapperAnnotation
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(U UpdateRequest, @MappingTarget E entity);



 
}
