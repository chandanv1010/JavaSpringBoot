package chandanv.local.chandanv.modules.users.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chandanv.local.chandanv.controllers.BaseController;
import chandanv.local.chandanv.enums.PermissionEnum;
import chandanv.local.chandanv.modules.users.entities.User;
import chandanv.local.chandanv.modules.users.mappers.UserMapper;
import chandanv.local.chandanv.modules.users.repositories.UserRepository;
import chandanv.local.chandanv.modules.users.requests.User.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.User.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.UserResource;
import chandanv.local.chandanv.modules.users.services.interfaces.UserServiceInterface;
import chandanv.local.chandanv.resources.ApiResource;
import jakarta.transaction.Transactional;


@RestController
@RequestMapping("api/v1/users")
public class UserController extends BaseController<
    User,
    UserResource,
    StoreRequest,
    UpdateRequest,
    UserRepository
> {

    @Autowired
    private UserRepository userRepository;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

     public UserController(
        UserServiceInterface service,
        UserMapper mapper,
        UserRepository repo
    ){
        super(service, mapper, repo, PermissionEnum.USER);
    }

    @Transactional
    @GetMapping("/me")
    public ResponseEntity<?> me(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
       
        UserResource userResource = UserResource.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .phone(user.getPhone())
            // .users(user.getUserCatalogues())
            .build();

        ApiResource<UserResource> response = ApiResource.ok(userResource, "SUCCESS");
        logger.info("Success!");
        return ResponseEntity.ok(response);

    }
    
}

