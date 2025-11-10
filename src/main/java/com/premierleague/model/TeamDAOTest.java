package com.premierleague.model;

import com.premierleague.dao.TeamDAO;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TeamDAOTest {

    @Test
    public void testLookupStandings() {
        TeamDAO teamDAO = new TeamDAO();
        List<Team> teams = teamDAO.lookupStandings();
        teams.forEach(t -> System.out.println(t.getTeamName() + " - Points: " + t.getPoints()));
        assertFalse(teams.isEmpty(), "Teams list should not be empty");
    }
}
