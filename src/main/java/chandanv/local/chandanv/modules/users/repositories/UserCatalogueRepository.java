package chandanv.local.chandanv.modules.users.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chandanv.local.chandanv.modules.users.entities.UserCatalogue;

@Repository
public interface UserCatalogueRepository extends JpaRepository<UserCatalogue, Long> {
    
}
