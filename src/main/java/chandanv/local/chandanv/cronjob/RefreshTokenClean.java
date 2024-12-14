package chandanv.local.chandanv.cronjob;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import chandanv.local.chandanv.modules.users.repositories.RefreshTokenRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;



@Service
public class RefreshTokenClean {
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(BlacklistTokenClean.class);


    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens(){
        LocalDateTime currentDataTime = LocalDateTime.now();
        int deletedCount = refreshTokenRepository.deleteByExpiryDateBefore(currentDataTime);
        logger.info("Đã xóa: " + deletedCount + " token");
    }

}
