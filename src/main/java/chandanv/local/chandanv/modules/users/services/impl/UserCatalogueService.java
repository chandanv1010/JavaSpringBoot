package chandanv.local.chandanv.modules.users.services.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.mappers.UserCatalogueMapper;
import chandanv.local.chandanv.modules.users.repositories.UserCatalogueRepository;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;
import chandanv.local.chandanv.modules.users.services.interfaces.UserCatalogueServiceInterface;
import chandanv.local.chandanv.services.BaseService;

@Service
public class UserCatalogueService extends BaseService<
    UserCatalogue, 
    UserCatalogueMapper, 
    StoreRequest, 
    UpdateRequest,
    UserCatalogueRepository
> implements  UserCatalogueServiceInterface {
    
    private final UserCatalogueMapper userCatalogueMapper;
    
    @Autowired
    private UserCatalogueRepository userCatalogueRepository;
    
    public UserCatalogueService(
        UserCatalogueMapper userCatalogueMapper
    ){
        this.userCatalogueMapper = userCatalogueMapper;
    }

    @Override
    protected String[] getSearchFields(){
        return new String[]{"name"};
    }

    @Override
    protected String[] getRelations(){
        return new String[]{"permissions", "users"};
    }
   
    @Override
    protected UserCatalogueRepository getRepository(){
        return userCatalogueRepository;
    }

    @Override
    protected UserCatalogueMapper getMapper(){
        return userCatalogueMapper;
    }
    

}
