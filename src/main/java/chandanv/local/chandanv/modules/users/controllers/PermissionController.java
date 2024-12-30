package chandanv.local.chandanv.modules.users.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chandanv.local.chandanv.controllers.BaseController;
import chandanv.local.chandanv.enums.PermissionEnum;
import chandanv.local.chandanv.modules.users.entities.Permission;
import chandanv.local.chandanv.modules.users.mappers.PermissionMapper;
import chandanv.local.chandanv.modules.users.repositories.PermissionRepository;
import chandanv.local.chandanv.modules.users.requests.Permission.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.Permission.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.PermissionResource;
import chandanv.local.chandanv.modules.users.services.interfaces.PermissionServiceInterface;


@Validated
@RestController
@RequestMapping("api/v1/permissions")
public class PermissionController extends BaseController<
    Permission,
    PermissionResource,
    StoreRequest,
    UpdateRequest,
    PermissionRepository
> {
    public PermissionController(
        PermissionServiceInterface service,
        PermissionMapper mapper,
        PermissionRepository repo
    ){
        super(service, mapper, repo, PermissionEnum.PERMISSION);
    }
}
