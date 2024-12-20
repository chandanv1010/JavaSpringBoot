package chandanv.local.chandanv.modules.users.services.impl;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chandanv.local.chandanv.helpers.FilterParameter;
import chandanv.local.chandanv.modules.users.controllers.UserCatalogueController;
import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.repositories.UserCatalogueRepository;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;
import chandanv.local.chandanv.modules.users.services.interfaces.UserCatalogueServiceInterface;
import chandanv.local.chandanv.services.BaseService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserCatalogueService extends BaseService implements  UserCatalogueServiceInterface {
    
    private static final Logger logger = LoggerFactory.getLogger(UserCatalogueController.class);
    
    @Autowired
    private UserCatalogueRepository userCatalogueRepository;

    //http://localhost:8080/api/v1/user_catalogues?perpage=20&page=2&sort=name,asc&keyword=123&publish=2&abc=4&def=5&price[gte]=100000
    @Override
    public Page<UserCatalogue> paginate(Map<String, String[]> parameters) {
        int page = parameters.containsKey("page") ? Integer.parseInt(parameters.get("page")[0]) : 1;
        int perpage = parameters.containsKey("perpage") ? Integer.parseInt(parameters.get("perpage")[0]) : 1;
        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null;
        Sort sort  = createSort(sortParam);

        String keyword = FilterParameter.filterKeyword(parameters);
        Map<String, String> filterSimple = FilterParameter.filterSimple(parameters);
        Map<String, Map<String, String>> filterComplex = FilterParameter.filterComplex(parameters);

        logger.info("keyword: " + keyword);
        logger.info("filterSimple: {}", filterSimple);
        logger.info("filterComplex: {}", filterComplex);



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
