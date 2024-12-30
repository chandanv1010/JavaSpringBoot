package chandanv.local.chandanv.modules.users.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chandanv.local.chandanv.modules.users.entities.UserCatalogue;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository
public interface UserCatalogueRepository extends JpaRepository<UserCatalogue, Long>, JpaSpecificationExecutor<UserCatalogue> {
    
}
