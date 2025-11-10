package com.premierleague.dao;

import com.premierleague.model.Match;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {
    private Connection conn;

    public MatchDAO(Connection conn) {
        this.conn = conn;
    }

    // Lookup matches for a team
    public List<Match> lookupTeamMatches(int teamId) throws SQLException {
        List<Match> matches = new ArrayList<>();
        String sql = "{call LookupTeamMatches(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Currently using IDs; can add team names later
                matches.add(new Match(
                        rs.getInt("match_id"),
                        rs.getInt("home_team_id"),
                        rs.getInt("away_team_id"),
                        rs.getInt("home_score"),
                        rs.getInt("away_score")
                ));
            }
        }
        return matches;
    }

    // Add a Match
    public void addMatch(int homeTeamId, int awayTeamId, int homeScore, int awayScore) throws SQLException {
        String sql = "{call AddMatch(?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, homeTeamId);
            stmt.setInt(2, awayTeamId);
            stmt.setInt(3, homeScore);
            stmt.setInt(4, awayScore);
            stmt.executeUpdate();
        }
    }

    // Edit a match
    public void editMatch(int matchId, int homeScore, int awayScore) throws SQLException {
        String sql = "{call EditMatch(?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, matchId);
            stmt.setInt(2, homeScore);
            stmt.setInt(3, awayScore);
            stmt.executeUpdate();
        }
    }

    // Delete a match
    public void deleteMatch(int matchId) throws SQLException {
        String sql = "{call DeleteMatch(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, matchId);
            stmt.executeUpdate();
        }
    }
}
