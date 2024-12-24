package chandanv.local.chandanv.modules.users.controllers;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import chandanv.local.chandanv.modules.users.mappers.UserCatalogueMapper;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.StoreRequest;
import chandanv.local.chandanv.modules.users.requests.UserCatalogue.UpdateRequest;
import chandanv.local.chandanv.modules.users.resources.UserCatalogueResource;
import chandanv.local.chandanv.modules.users.services.interfaces.UserCatalogueServiceInterface;
import chandanv.local.chandanv.resources.ApiResource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Validated
@RestController
@RequestMapping("api/v1")
public class UserCatalogueController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserCatalogueController.class);
    private final UserCatalogueServiceInterface userCatalogueService;
    private final UserCatalogueMapper userCatalogueMapper;
    
   
    public UserCatalogueController(
        UserCatalogueServiceInterface userCatalogueService,
        UserCatalogueMapper userCatalogueMapper
    ){
        this.userCatalogueService = userCatalogueService;
        this.userCatalogueMapper = userCatalogueMapper;
    }

    @GetMapping("user_catalogues/list")
    public ResponseEntity<?> list(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        List<UserCatalogue> userCatalogues = userCatalogueService.getAll(parameters);
        List<UserCatalogueResource> userCataloguesResource = userCatalogueMapper.toList(userCatalogues);
        ApiResource<List<UserCatalogueResource>> response = ApiResource.ok(userCataloguesResource, "SUCCESS");
        logger.info("Method index Running....!");
        return ResponseEntity.ok(response); 
    }
    
    @GetMapping("user_catalogues")
    public ResponseEntity<?> pagination(HttpServletRequest request){
        Map<String, String[]> parameters = request.getParameterMap();
        Page<UserCatalogue> userCatalogues = userCatalogueService.paginate(parameters);
        Page<UserCatalogueResource> userCataloguesResource = userCatalogueMapper.toResourcePage(userCatalogues);
        ApiResource<Page<UserCatalogueResource>> response = ApiResource.ok(userCataloguesResource, "SUCCESS");
        logger.info("Method index Running....!");
        return ResponseEntity.ok(response); 
    } 

    @PostMapping("/user_catalogues")
    public ResponseEntity<?> store(@Valid @RequestBody StoreRequest request){
        UserCatalogue userCatalogue = userCatalogueService.create(request);
        UserCatalogueResource userCatalogueResource = userCatalogueMapper.tResource(userCatalogue);

        ApiResource<UserCatalogueResource> response = ApiResource.ok(userCatalogueResource, "Thêm mới bản ghi thành công");
        logger.info("Method Store Running....!");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user_catalogues/{id}")
    public ResponseEntity<?> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateRequest request
    ){
        logger.info("Method Update Running....!");
        try {
            UserCatalogue userCatalogue = userCatalogueService.update(id, request);
            UserCatalogueResource userCatalogueResource = userCatalogueMapper.tResource(userCatalogue);
            ApiResource<UserCatalogueResource> response = ApiResource.ok(userCatalogueResource, "Cập nhật bản ghi thành công");
            return ResponseEntity.ok(response);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResource.error("NOT_FOUND", e.getMessage(), HttpStatus.NOT_FOUND)
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResource.error("INTERNAL_SERVER_ERROR", "Có lỗi xảy ra trong quá trình cập nhật", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

}
