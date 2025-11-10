package com.premierleague.dao;

import com.premierleague.Database;
import com.premierleague.model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {
    private Connection conn;

     public TeamDAO() {
        this.conn = Database.getConnection();
    }

    public TeamDAO(Connection conn) {
        this.conn = conn;
    }

    // Fetch all teams for standings table
    public List<Team> lookupStandings() {
        List<Team> teams = new ArrayList<>();
        String sql = "{CALL lookupStandings()}";  // stored procedure returning all teams from standings view
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Team t = (new Team(
                        rs.getInt("team_id"),
                        rs.getString("team_name"),
                        rs.getInt("matches_played"),
                        rs.getInt("wins"),
                        rs.getInt("draws"),
                        rs.getInt("losses"),
                        rs.getInt("goals_for"),
                        rs.getInt("goals_against"),
                        rs.getInt("goal_difference"),
                        rs.getInt("points")
                ));
                System.out.println("Loaded team: " + t.getTeamName());
                teams.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }

    // Fetch a single team by teamId
    public Team lookupTeamStandings(int teamId) {
        String sql = "{CALL LookupTeamStandings(?)}";  // stored procedure for individual team
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Team(
                        rs.getInt("team_id"),
                        rs.getString("team_name"),
                        rs.getInt("matches_played"),
                        rs.getInt("wins"),
                        rs.getInt("draws"),
                        rs.getInt("losses"),
                        rs.getInt("goals_for"),
                        rs.getInt("goals_against"),
                        rs.getInt("goal_difference"),
                        rs.getInt("points")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // return null if team not found
    }
}
