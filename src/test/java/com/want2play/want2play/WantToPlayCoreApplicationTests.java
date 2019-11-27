package com.want2play.want2play;

import com.want2play.want2play.service.AdministrationService;
import com.want2play.want2play.service.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class WantToPlayCoreApplicationTests {

	@Autowired
	AdministrationService administrationService;

	@Autowired
	MatchService matchService;

	@Test
	void contextLoads() {
		assertThat(administrationService).isNotNull();
		assertThat(matchService).isNotNull();
	}

}
