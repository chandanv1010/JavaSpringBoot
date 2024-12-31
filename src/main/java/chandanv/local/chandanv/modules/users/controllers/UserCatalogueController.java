package chandanv.local.chandanv.modules.users.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chandanv.local.chandanv.controllers.BaseController;
import chandanv.local.chandanv.enums.PermissionEnum;

import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.mappers.UserCatalogueMapper;
import chandanv.local.chandanv.modules.users.repositories.UserCatalogueRepository;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.UserCatalogueResource;
import chandanv.local.chandanv.modules.users.services.interfaces.UserCatalogueServiceInterface;


@Validated
@RestController
@RequestMapping("api/v1/user_catalogues")
public class UserCatalogueController extends BaseController<
    UserCatalogue,
    UserCatalogueResource,
    StoreRequest,
    UpdateRequest,
    UserCatalogueRepository
> {
    public UserCatalogueController(
        UserCatalogueServiceInterface service,
        UserCatalogueMapper mapper,
        UserCatalogueRepository repo
    ){
        super(service, mapper, repo, PermissionEnum.USER_CATALOGUE);
    }
    
    
}
