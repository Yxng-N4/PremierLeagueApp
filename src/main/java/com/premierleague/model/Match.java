package com.premierleague.model;

public class Match {
    // --- Fields (data about a match) ---
    private int matchId;
    private int homeTeamId;
    private int awayTeamId;
    private String homeTeamName;   
    private String awayTeamName;   
    private int homeScore;
    private int awayScore;
    private String matchDescription;


    // --- Constructor (creates a Match object with values) ---
   public Match(int matchId, String homeTeamName, String awayTeamName, int homeScore, int awayScore, String matchDescription) {
        this.matchId = matchId;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchDescription = homeTeamName + " " + homeScore + " - " + awayScore + " " + awayTeamName;
    }

    // Constructor for DB operations (IDs)
    public Match(int matchId, int homeTeamId, int awayTeamId, int homeScore, int awayScore) {
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchDescription = ""; 
    }

    // --- Getters (read-only access to private fields) ---
    public int getMatchId() { return matchId; }
    public int getHomeTeamId() { return homeTeamId; }
    public int getAwayTeamId() { return awayTeamId; }
    public String getHomeTeamName() { return homeTeamName; }
    public String getAwayTeamName() { return awayTeamName; }
    public int getHomeScore() { return homeScore; }
    public int getAwayScore() { return awayScore; }
    public String getMatchDescription() { return matchDescription; }

    // --- Setters (optional: allow updating after creation) ---
    public void setHomeScore(int homeScore) { this.homeScore = homeScore; }
    public void setAwayScore(int awayScore) { this.awayScore = awayScore; }

    // --- toString (makes printing the object readable) ---
    @Override
    public String toString() {
        return matchDescription;
    }
}
