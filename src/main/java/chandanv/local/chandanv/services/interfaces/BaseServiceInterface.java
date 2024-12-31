package chandanv.local.chandanv.services.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import jakarta.servlet.http.HttpServletRequest;


public interface BaseServiceInterface <E, C, U> {
    
    E create(C request);
    E update(Long id, U request);
    Boolean delete(Long id);
    Boolean deleteMultipleEntity(List<Long> ids);
    Page<E> paginate(Map<String, String[]> parameters, HttpServletRequest request);
    List<E> getAll(Map<String, String[]> parameters, HttpServletRequest request);

}
