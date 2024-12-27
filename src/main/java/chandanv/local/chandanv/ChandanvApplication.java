package chandanv.local.chandanv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages="chandanv.local.chandanv.modules")
public class ChandanvApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChandanvApplication.class, args);
	}

}
