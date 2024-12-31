package chandanv.local.chandanv.services;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chandanv.local.chandanv.helpers.FilterParameter;
import chandanv.local.chandanv.mappers.BaseMapper;
import chandanv.local.chandanv.specifications.BaseSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;


@Service
public abstract class BaseService <
    T, 
    M extends BaseMapper<T, ?, C, U>, 
    C, 
    U,
    R extends JpaRepository<T, Long> & JpaSpecificationExecutor<T>
> {

    @Autowired
    private ApplicationContext applicationContext;
  
    private final Logger logger = LoggerFactory.getLogger(getClass());
   
    protected abstract String[] getSearchFields();
    protected String[] getRelations(){
        return new String[0];
    }
    protected abstract R getRepository();
    protected abstract M getMapper();

    protected void preProcessRequest(C request){

    }


    private Map<String, String[]> modifiedParameters(HttpServletRequest request, Map<String, String[]> parameters){

        Map<String, String[]> modifedParameters = new HashMap<>(parameters);

        Object userIdAttribute = request.getAttribute("userId");
        if(userIdAttribute != null){
            String userId = userIdAttribute.toString();
            modifedParameters.put("userId", new String[]{userId});
        }

        return modifedParameters;
    }
    
    public List<T> getAll(Map<String, String[]> parameters, HttpServletRequest request){
        Map<String, String[]> modifiedParameters = modifiedParameters(request, parameters);
        Sort sort = parseSort(modifiedParameters);
        Specification<T> specs = buildSpecification(modifiedParameters, getSearchFields());
        return getRepository().findAll(specs, sort);
    }

    public Page<T> paginate(Map<String, String[]> parameters, HttpServletRequest request) {
        Map<String, String[]> modifiedParameters = modifiedParameters(request, parameters);
        int page = modifiedParameters.containsKey("page") ? Integer.parseInt(modifiedParameters.get("page")[0]) : 1;
        int perpage = modifiedParameters.containsKey("perpage") ? Integer.parseInt(modifiedParameters.get("perpage")[0]) : 20;
        Sort sort  = parseSort(modifiedParameters);
        Specification<T> specs = buildSpecification(modifiedParameters, getSearchFields());

        Pageable pageable = PageRequest.of(page - 1, perpage, sort);
        return getRepository().findAll(specs, pageable);
    } 

    @Transactional
    public T create(C request){
        logger.info("Creating....");
        preProcessRequest(request);
        T payload = getMapper().toEntity(request);
        T entity = getRepository().save(payload);
        handleManyToManyRelations(entity, request);

        return entity;
    } 

    @Transactional
    public T update(Long id, U request){
        T entity = getRepository().findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Bản ghi không tồn tại"));
        getMapper().updateEntityFromRequest(request, entity);
        T entityUpdate = getRepository().save(entity);
        handleManyToManyRelations(entityUpdate, request);
        return entityUpdate;
    }


    @Transactional
    public Boolean delete(Long id){
        getRepository().findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Bản ghi không tồn tại"));
        getRepository().deleteById(id);
        return true;
    }

    @Transactional
    public Boolean deleteMultipleEntity(List<Long> Ids){
        List<T> entities = getRepository().findAllById(Ids);
        if(entities.size() != Ids.size()){
            throw new RuntimeException("Số lượng bản ghi cần xóa không khớp");
        }
        getRepository().deleteAll(entities);
        return true;
    }

    protected Sort parseSort(Map<String, String[]> parameters){
        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null;
        return createSort(sortParam);
    }

    protected Specification<T> buildSpecification(Map<String, String[]> parameters, String[] searchFields){
        String keyword = FilterParameter.filterKeyword(parameters);
        Map<String, String> filterSimple = FilterParameter.filterSimple(parameters);

        logger.info("filterSimple: {}", filterSimple);

        Map<String, Map<String, String>> filterComplex = FilterParameter.filterComplex(parameters);

        Specification<T> specs = Specification.where(
            BaseSpecification.<T>keywordSpec(keyword, searchFields)
        )
        .and(BaseSpecification.<T>whereSpec(filterSimple))
        .and(BaseSpecification.<T>complexWhereSpec(filterComplex));

        return specs;
    }

    private void handleManyToManyRelations(T entity, Object request){
        String[] relations = getRelations();
        if(relations != null && relations.length > 0){
            for(String relation: relations){
                try {
                    Field requestField = request.getClass().getDeclaredField(relation);
                    requestField.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<Long> ids = (List<Long>) requestField.get(request);
                    if(ids != null && !ids.isEmpty()){
                        Field entityField = entity.getClass().getDeclaredField(relation);
                        entityField.setAccessible(true);
                        ParameterizedType setType = (ParameterizedType) entityField.getGenericType();
                        Class<?> entityClass = (Class<?>) setType.getActualTypeArguments()[0];
                        String repositoryName = entityClass.getSimpleName() + "Repository";
                        repositoryName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);
                        
                        @SuppressWarnings("unchecked")
                        JpaRepository<T, Long> repository = (JpaRepository<T, Long>) applicationContext.getBean(repositoryName);
                        List<T> entities = repository.findAllById(ids);
                        Set<T> entitySet = new HashSet<>(entities);
                        entityField.set(entity, entitySet);
                    }
                    
                }catch ( NoSuchFieldException | ClassCastException | IllegalAccessException  e) {
                    throw new RuntimeException("Lỗi xảy ra khi xử lý quan hệ: " + relation + " " + e.getMessage(), e);
                }
            }
        }
    }


    protected Sort createSort(String sortParam){
        if(sortParam == null || sortParam.isEmpty()){
            return Sort.by(Sort.Order.desc("id"));
        }
        String[] parts = sortParam.split(",");
        String field = parts[0];
        String sortDirection = (parts.length > 1) ? parts[1] : "asc";

        if("desc".equalsIgnoreCase(sortDirection)){
            return Sort.by(Sort.Order.desc(field));
        }else{
            return Sort.by(Sort.Order.asc(field));
        }
    }



}
