package chandanv.local.chandanv.modules.users.services.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chandanv.local.chandanv.modules.users.entities.Permission;
import chandanv.local.chandanv.modules.users.mappers.PermissionMapper;
import chandanv.local.chandanv.modules.users.repositories.PermissionRepository;
import chandanv.local.chandanv.modules.users.requests.Permission.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.Permission.UpdateRequest;
import chandanv.local.chandanv.modules.users.services.interfaces.PermissionServiceInterface;
import chandanv.local.chandanv.services.BaseService;

import chandanv.local.chandanv.enums.PermissionEnum;

@Service
public class PermissionService extends BaseService<
    Permission, 
    PermissionMapper, 
    StoreRequest, 
    UpdateRequest,
    PermissionRepository
> implements  PermissionServiceInterface {
    
    private final PermissionMapper PermissionMapper;
    
    @Autowired
    private PermissionRepository PermissionRepository;
    
    public PermissionService(
        PermissionMapper PermissionMapper
    ){
        this.PermissionMapper = PermissionMapper;
    }

    @Override
    protected String[] getSearchFields(){
        return new String[]{"name"};
    }

    @Override
    protected PermissionRepository getRepository(){
        return PermissionRepository;
    }

    @Override
    protected PermissionMapper getMapper(){
        return PermissionMapper;
    }

    public boolean hasPermission(String requiredPermission, PermissionEnum module, String action){
        String permission = module.getPrefix() + ":" + action;
        return requiredPermission.equals(permission);
    }

}
