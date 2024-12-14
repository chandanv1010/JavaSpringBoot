package chandanv.local.chandanv.modules.users.services.impl;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.repositories.UserCatalogueRepository;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;
import chandanv.local.chandanv.modules.users.services.interfaces.UserCatalogueServiceInterface;
import chandanv.local.chandanv.services.BaseService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserCatalogueService extends BaseService implements  UserCatalogueServiceInterface {
    

    @Autowired
    private UserCatalogueRepository userCatalogueRepository;

    // http://localhost:8080/user_catalogues?keyword=abc&publish=1&perpage=40&sort=name,asc | id,desc | ...
    @Override
    public Page<UserCatalogue> paginate(Map<String, String[]> parameters) {
        int page = parameters.containsKey("page") ? Integer.parseInt(parameters.get("page")[0]) : 1;
        int perpage = parameters.containsKey("perpage") ? Integer.parseInt(parameters.get("perpage")[0]) : 1;
        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null;
        Sort sort  = createSort(sortParam);
        Pageable pageable = PageRequest.of(page - 1, perpage, sort);
        return userCatalogueRepository.findAll(pageable);
    }


    @Override
    @Transactional
    public UserCatalogue create(StoreRequest request){
        try {
            UserCatalogue payload = UserCatalogue.builder()
                .name(request.getName())
                .publish(request.getPublish())
                .build();

            return userCatalogueRepository.save(payload);
            
        } catch (Exception e) {
            throw new RuntimeException("Transaction failed:" + e.getMessage());
        }
    }


    @Override
    @Transactional
    public UserCatalogue update(Long id, UpdateRequest request){

        UserCatalogue userCatalogue = userCatalogueRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Nhóm thành viên không tồn tại"));

        UserCatalogue payload = userCatalogue.toBuilder()
            .name(request.getName())
            .publish(request.getPublish())
            .build();
        
        return userCatalogueRepository.save(payload);
    }
}
