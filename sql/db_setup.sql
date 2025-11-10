
-- Standings View Creation
INSERT INTO Standings (team_id, team_name, points, goals_for, goals_against)
SELECT team_id, team_name, 0, 0, 0
FROM Teams
WHERE team_id NOT IN (SELECT team_id FROM Standings);
CREATE OR REPLACE VIEW Standings AS
WITH results AS (
    SELECT 
        t.team_id,
        t.team_name,
        CASE 
            WHEN t.team_id = m.home_team_id AND home_score > m.away_score THEN 1
            WHEN t.team_id = m.away_team_id AND m.away_score > home_score THEN 1
            ELSE 0
        END AS win,
        CASE 
            WHEN home_score = m.away_score THEN 1
            ELSE 0
        END AS draw,
        CASE 
            WHEN t.team_id = m.home_team_id AND home_score < m.away_score THEN 1
            WHEN t.team_id = m.away_team_id AND m.away_score < home_score THEN 1
            ELSE 0
        END AS loss,
        CASE WHEN t.team_id = m.home_team_id THEN home_score
             WHEN t.team_id = m.away_team_id THEN m.away_score
        END AS goals_for,
        CASE WHEN t.team_id = m.home_team_id THEN m.away_score
             WHEN t.team_id = m.away_team_id THEN home_score
        END AS goals_against
    FROM teams t
    LEFT JOIN matches m 
    ON t.team_id IN (m.home_team_id, m.away_team_id)
)
SELECT
    team_id,
    team_name,
    SUM(win + draw + loss) AS matches_played,
    SUM(win) AS wins,
    SUM(draw) AS draws,
    SUM(loss) AS losses,
    SUM(goals_for) AS goals_for,
    SUM(goals_against) AS goals_against,
    SUM(goals_for - goals_against) AS goal_difference,
    SUM(win*3 + draw*1) AS points
FROM results
GROUP BY team_id, team_name
ORDER BY points DESC, goal_difference DESC;

--Add Match Stored Procedure
CREATE PROCEDURE AddMatch(
    IN home_id INT,
    IN away_id INT,
    IN home_score INT,
    IN away_score INT
)
BEGIN
    IF home_id = away_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'A team cannot play against itself';
    ELSE
        INSERT INTO matches (home_team_id, away_team_id, home_score, away_score)
        VALUES (home_id, away_id, home_score, away_score);
    END IF;
END //

--Edit Match Stored Procedure
CREATE PROCEDURE EditMatch(
    IN matchID INT,
    IN new_home_score INT,
    IN new_away_score INT
)
BEGIN
    UPDATE matches
    SET home_score = new_home_score,
        away_score = new_away_score
    WHERE match_id = matchID;
END //

--Delete Match Stored Procedure
CREATE PROCEDURE DeleteMatch(
    IN matchID INT
)
BEGIN
    DELETE FROM matches
    WHERE match_id = matchID;
END //

--Team Lookup Stored Procedure
CREATE PROCEDURE LookupTeamStandings(IN p_team_id INT)
BEGIN
    SELECT 
        team_id,
        team_name,
        played,
        wins,
        draws,
        losses,
        gf AS goals_for,
        ga AS goals_against,
        gd AS goal_difference,
        points
    FROM Standings
    WHERE team_id = p_team_id;
END //

--Lookup Team Matches Stored Procedure
CREATE PROCEDURE LookupTeamMatches(IN p_team_id INT)
BEGIN
    SELECT 
        match_id,
        home_team_id,
        away_team_id,
        home_score,
        away_score
    FROM Matches
    WHERE home_team_id = p_team_id OR away_team_id = p_team_id
    ORDER BY match_id;
END //

--Lookup All Teams Stored Procedure
CREATE PROCEDURE LookupStandings()
BEGIN
    SELECT 
        team_id,
        team_name,
        played,
        wins,
        draws,
        losses,
        gf AS goals_for,
        ga AS goals_against,
        gd AS goal_difference,
        points
    FROM Standings
    ORDER BY points DESC, gd DESC, gf DESC; 
END //