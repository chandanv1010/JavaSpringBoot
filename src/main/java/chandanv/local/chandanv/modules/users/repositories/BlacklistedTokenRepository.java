package chandanv.local.chandanv.modules.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chandanv.local.chandanv.modules.users.entities.BlacklistedToken;
import java.time.LocalDateTime;


@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
   boolean existsByToken(String token);

   int deleteByExpiryDateBefore(LocalDateTime currentDateTime);

   // int deleteEx
}
