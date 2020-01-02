package com.want2play.want2play;

import com.want2play.want2play.service.LocationService;
import com.want2play.want2play.service.MatchService;
import com.want2play.want2play.service.PlayerService;
import com.want2play.want2play.service.SportService;
import com.want2play.want2play.service.StadiumService;
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
    private LocationService locationService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private SportService sportService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private StadiumService stadiumService;

    @Test
    void contextLoads() {
        assertThat(locationService).isNotNull();
        assertThat(matchService).isNotNull();
        assertThat(sportService).isNotNull();
        assertThat(playerService).isNotNull();
        assertThat(stadiumService).isNotNull();
    }

}
