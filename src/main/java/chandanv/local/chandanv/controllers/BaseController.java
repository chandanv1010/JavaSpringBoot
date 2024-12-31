package chandanv.local.chandanv.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import chandanv.local.chandanv.annotations.RequirePermission;
import chandanv.local.chandanv.enums.PermissionEnum;
import chandanv.local.chandanv.mappers.BaseMapper;
import chandanv.local.chandanv.services.interfaces.BaseServiceInterface;
import chandanv.local.chandanv.resources.ApiResource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public abstract class BaseController <
    E,
    R,
    C,
    U,
    Rp extends JpaRepository<E, Long> & JpaSpecificationExecutor<E>
> {
    
    protected final BaseServiceInterface<E, C, U> service;
    protected final BaseMapper<E, R, C, U> mapper;
    protected final Rp repo;
    public final PermissionEnum module;


    public BaseController(BaseServiceInterface<E, C, U> service, BaseMapper<E, R, C, U> mapper, Rp repo, PermissionEnum module){
        this.service = service;
        this.mapper = mapper;
        this.repo = repo;
        this.module = module;
    }

    public PermissionEnum getModule() {
        return module;
    }


    @GetMapping("/list")
    // @RequirePermission(action = "list", viewAll="view_all")
    public ResponseEntity<?> list(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        List<E> entities = service.getAll(parameters, request);
        List<R> resource = mapper.toList(entities);
        ApiResource<List<R>> response = ApiResource.ok(resource, "SUCCESS");
        return ResponseEntity.ok(response); 
    }

    @GetMapping
    // @RequirePermission(action = "pagination", viewAll="view_all")
    public ResponseEntity<?> pagination(HttpServletRequest request){
        Map<String, String[]> parameters = request.getParameterMap();
        Page<E> entities = service.paginate(parameters, request);
        Page<R> resource = mapper.toResourcePage(entities);
        ApiResource<Page<R>> response = ApiResource.ok(resource, "SUCCESS");
        return ResponseEntity.ok(response); 
    } 

    @PostMapping
    @RequirePermission(action = "store")
    public ResponseEntity<?> store(@Valid @RequestBody C request){
        try {
            E entity = service.create(request);
            R resource = mapper.tResource(entity);
            ApiResource<R> response = ApiResource.ok(resource, "Thêm mới bản ghi thành công");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            String message = "Có lỗi xảy ra trong quá trình xử lý " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResource.error("INTERNAL_SERVER_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @PutMapping("/{id}")
    @RequirePermission(action = "update")
    public ResponseEntity<?> update(
        @PathVariable("id") Long id,
        @Valid @RequestBody U request
    ){
        try {
            E entity = service.update(id, request);
            R resource = mapper.tResource(entity);
            ApiResource<R> response = ApiResource.ok(resource, "Cập nhật bản ghi thành công");
            return ResponseEntity.ok(response);
            
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResource.error("NOT_FOUND", e.getMessage(), HttpStatus.NOT_FOUND)
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResource.error("INTERNAL_SERVER_ERROR", "Có lỗi xảy ra trong quá trình cập nhật " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @GetMapping("/{id}")
    @RequirePermission(action = "show")
    public ResponseEntity<?> show( @PathVariable Long id) {
        E entity = repo.findById(id).orElseThrow(() ->new RuntimeException("Bản ghi không tồn tại"));
        R resource = mapper.tResource(entity);
        ApiResource<R> response = ApiResource.ok(resource, "SUCCESS");
        return ResponseEntity.ok(response); 
    }

    @DeleteMapping("/{id}")
    @RequirePermission(action = "delete")
    public ResponseEntity<?> delete( @PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(ApiResource.message("Xóa bản ghi thành công", HttpStatus.OK));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResource.error("NOT_FOUND", e.getMessage(), HttpStatus.NOT_FOUND)
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResource.error("INTERNAL_SERVER_ERROR", "Có lỗi xảy ra trong quá trình xử lý", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

    @DeleteMapping
    @RequirePermission(action = "deleteMany")
    public ResponseEntity<?> deleteMany(@RequestBody List<Long> Ids){
        try {
            service.deleteMultipleEntity(Ids);
            return ResponseEntity.ok(ApiResource.message("Xóa bản ghi thành công", HttpStatus.OK));
            
        } catch (RuntimeException e) {
            String message = e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResource.error("NOT_FOUND", message, HttpStatus.NOT_FOUND)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResource.error("INTERNAL_SERVER_ERROR", "Có lỗi xảy ra trong quá trình xử lý", HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
    }

  
}
