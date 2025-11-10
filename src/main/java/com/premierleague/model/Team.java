package com.premierleague.model;

public class Team {
    private int teamId;
    private String teamName;
    private int played;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private int points;

    // Constructor
    public Team(int teamId, String teamName, int played, int wins, int draws, int losses,
                int goalsFor, int goalsAgainst, int goalDifference, int points) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.played = played;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.goalDifference = goalDifference;
        this.points = points;
    }

    // Getters
    public int getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }
    public int getPlayed() { return played; }
    public int getWins() { return wins; }
    public int getDraws() { return draws; }
    public int getLosses() { return losses; }
    public int getGoalsFor() { return goalsFor; }
    public int getGoalsAgainst() { return goalsAgainst; }
    public int getGoalDifference() { return goalDifference; }
    public int getPoints() { return points; }

    @Override
    public String toString() {
        return teamName + " - Points: " + points + " GD: " + goalDifference;
    }
}
