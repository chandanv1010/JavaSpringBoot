package chandanv.local.chandanv.modules.users.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chandanv.local.chandanv.modules.users.entities.Permission;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository("permissionRepository")
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    
}
