package chandanv.local.chandanv.services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import chandanv.local.chandanv.modules.users.controllers.UserCatalogueController;



@Service
public class BaseService {

    private static final Logger logger = LoggerFactory.getLogger(UserCatalogueController.class);
    
    protected Sort createSort(String sortParam){

        logger.info("sortParam " + sortParam);

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
