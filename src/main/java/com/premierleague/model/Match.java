package com.premierleague.model;

public class Match {
    // --- Fields (data about a match) ---
    private int matchId;
    private int homeTeamId;
    private int awayTeamId;
    private int homeScore;
    private int awayScore;

    // --- Constructor (creates a Match object with values) ---
    public Match(int matchId, int homeTeamId, int awayTeamId, int homeScore, int awayScore) {
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    // --- Getters (read-only access to private fields) ---
    public int getMatchId() { return matchId; }
    public int getHomeTeamId() { return homeTeamId; }
    public int getAwayTeamId() { return awayTeamId; }
    public int getHomeScore() { return homeScore; }
    public int getAwayScore() { return awayScore; }

    // --- Setters (optional: allow updating after creation) ---
    public void setHomeScore(int homeScore) { this.homeScore = homeScore; }
    public void setAwayScore(int awayScore) { this.awayScore = awayScore; }

    // --- toString (makes printing the object readable) ---
    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", homeTeamId=" + homeTeamId +
                ", awayTeamId=" + awayTeamId +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                '}';
    }
}
