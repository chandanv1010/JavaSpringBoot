package chandanv.local.chandanv.cronjob;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chandanv.local.chandanv.modules.users.repositories.BlacklistedTokenRepository;



@Service

public class BlacklistTokenClean {
    
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(BlacklistTokenClean.class);


    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens(){
        LocalDateTime currentDataTime = LocalDateTime.now();
        int deletedCount = blacklistedTokenRepository.deleteByExpiryDateBefore(currentDataTime);
        logger.info("Đã xóa: " + deletedCount + " token");
    }

}
