package com.want2play.want2play.integration;

import com.want2play.want2play.model.*;
import com.want2play.want2play.service.MatchService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MatchesIntegrationTest {

    @Autowired
    MatchService matchService;

    @Test
    public void saveAnInitialMatch() {
        // given
        Stadium stadium = new Stadium("Madison Square Garden", "5th Ave", "New York");
        stadium.getFields().add(new Field("Main Arena", "Basketball"));

        Team teamA = new Team(5);
        teamA.getPlayers().addAll(Arrays.asList(
                new Player("@1", "Player 1"),
                new Player("@2", "Player 2"),
                new Player("@3", "Player 3"),
                new Player("@4", "Player 4"),
                new Player("@5", "Player 5")));

        Team teamB = new Team(5);
        teamB.getPlayers().addAll(Arrays.asList(
                new Player("@6", "Player 6"),
                new Player("@7", "Player 7"),
                new Player("@8", "Player 8"),
                new Player("@9", "Player 9"),
                new Player("@10", "Player 10")));

        Match match = new Match();
        match.setStadium(stadium);
        match.setSport(new Sport("BSK", "Basketball", 5));
        match.setAdminPlayer(teamA.getPlayers().iterator().next());
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setSchedule(DateTime.now().toDate());
        match.setNotes("A test match in NY");

        // when
        matchService.saveMatch(match);

        List<Match> allMatches = matchService.getAllMatches();
        assertThat(allMatches);
        assertEquals(allMatches.get(0).getNotes(), "A test match in NY");
        assertEquals(allMatches.get(0).getId(), match.getId());
    }

}
