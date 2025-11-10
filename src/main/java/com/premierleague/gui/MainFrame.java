package com.premierleague.gui;

import com.premierleague.dao.TeamDAO;
import com.premierleague.Database;
import com.premierleague.dao.MatchDAO;
import com.premierleague.model.Team;
import com.premierleague.model.Match;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class MainFrame extends JFrame {

    private JTable standingsTable;
    private DefaultTableModel tableModel;
    private JTextArea logArea;

    private TeamDAO teamDAO;
    private MatchDAO matchDAO;

    public MainFrame() {
        super("Premier League 25/26 Standings");

        Connection conn = Database.getConnection();  // Method to get DB connection
        teamDAO = new TeamDAO(conn);
        matchDAO = new MatchDAO(conn);


        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        // Table setup
        String[] columns = {"Team", "Played", "Wins", "Draws", "Losses", "GF", "GA", "GD", "Points"};
        tableModel = new DefaultTableModel(columns, 0);
        standingsTable = new JTable(tableModel);
        standingsTable.setEnabled(false);  // prevent editing directly

        JScrollPane tableScroll = new JScrollPane(standingsTable);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0.7;
        add(tableScroll, gbc);

        // Log area
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        gbc.gridy = 1;
        gbc.weighty = 0.3;
        add(logScroll, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bGbc = new GridBagConstraints();
        bGbc.insets = new Insets(2,2,2,2);
        bGbc.fill = GridBagConstraints.HORIZONTAL;
        bGbc.gridx = 0;
        bGbc.gridy = 0;

        JButton addMatchBtn = new JButton("Add Match");
        addMatchBtn.addActionListener(e -> openAddMatchWindow());
        buttonPanel.add(addMatchBtn, bGbc);

        bGbc.gridy++;
        JButton editMatchBtn = new JButton("Edit Match");
        editMatchBtn.addActionListener(e -> openEditMatchWindow());
        buttonPanel.add(editMatchBtn, bGbc);

        bGbc.gridy++;
        JButton deleteMatchBtn = new JButton("Delete Match");
        deleteMatchBtn.addActionListener(e -> openDeleteMatchWindow());
        buttonPanel.add(deleteMatchBtn, bGbc);

        bGbc.gridy++;
        JButton lookupTeamBtn = new JButton("Lookup Team Matches");
        lookupTeamBtn.addActionListener(e -> openLookupTeamWindow());
        buttonPanel.add(lookupTeamBtn, bGbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        add(buttonPanel, gbc);

        // Populate the table initially
        refreshStandingsTable();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshStandingsTable() {
        tableModel.setRowCount(0);  // clear current table
        try {
            List<Team> teams = teamDAO.lookupStandings();  // assumes this returns teams sorted by points
            for (Team t : teams) {
                tableModel.addRow(new Object[]{
                        t.getTeamName(),
                        t.getPlayed(),
                        t.getWins(),
                        t.getDraws(),
                        t.getLosses(),
                        t.getGoalsFor(),
                        t.getGoalsAgainst(),
                        t.getGoalDifference(),
                        t.getPoints()
                });
            }
            logArea.append("Standings refreshed.\n");
        } catch (Exception ex) {
            logArea.append("Error refreshing standings: " + ex.getMessage() + "\n");
        }
    }

    // Add Button
    private void openAddMatchWindow() {
    logArea.append("Add Match clicked.\n");

    JDialog dialog = new JDialog(this, "Add Match", true);
    dialog.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5,5,5,5);

    // Home Team dropdown
    gbc.gridx = 0; gbc.gridy = 0;
    dialog.add(new JLabel("Home Team:"), gbc);
    JComboBox<Team> homeTeamBox = new JComboBox<>(teamDAO.lookupStandings().toArray(new Team[0]));
    gbc.gridx = 1;
    dialog.add(homeTeamBox, gbc);

    // Away Team dropdown
    gbc.gridx = 0; gbc.gridy = 1;
    dialog.add(new JLabel("Away Team:"), gbc);
    JComboBox<Team> awayTeamBox = new JComboBox<>(teamDAO.lookupStandings().toArray(new Team[0]));
    gbc.gridx = 1;
    dialog.add(awayTeamBox, gbc);

    // Home Score input
    gbc.gridx = 0; gbc.gridy = 2;
    dialog.add(new JLabel("Home Score:"), gbc);
    JTextField homeScoreField = new JTextField(3);
    gbc.gridx = 1;
    dialog.add(homeScoreField, gbc);

    // Away Score input
    gbc.gridx = 0; gbc.gridy = 3;
    dialog.add(new JLabel("Away Score:"), gbc);
    JTextField awayScoreField = new JTextField(3);
    gbc.gridx = 1;
    dialog.add(awayScoreField, gbc);

    // Submit button
    gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
    JButton submitBtn = new JButton("Add Match");
    dialog.add(submitBtn, gbc);

    submitBtn.addActionListener(e -> {
        try {
            Team homeTeam = (Team) homeTeamBox.getSelectedItem();
            Team awayTeam = (Team) awayTeamBox.getSelectedItem();
            int homeScore = Integer.parseInt(homeScoreField.getText());
            int awayScore = Integer.parseInt(awayScoreField.getText());

            // Call DAO to add match using stored procedure
            matchDAO.addMatch(homeTeam.getTeamId(), awayTeam.getTeamId(), homeScore, awayScore);

            logArea.append("Added match: " + homeTeam.getTeamName() + " " + homeScore + 
                    " - " + awayScore + " " + awayTeam.getTeamName() + "\n");

            refreshStandingsTable();  // reload table to show updated standings
            dialog.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
        }
    });

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

    private void openEditMatchWindow() {
    logArea.append("Edit Match clicked.\n");

    JDialog dialog = new JDialog(this, "Edit Match", true);
    dialog.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5,5,5,5);

    // Step 1: Select team
    gbc.gridx = 0; gbc.gridy = 0;
    dialog.add(new JLabel("Select Team:"), gbc);

    List<Team> teams = teamDAO.lookupStandings();
    JComboBox<Team> teamBox = new JComboBox<>(teams.toArray(new Team[0]));
    gbc.gridx = 1;
    dialog.add(teamBox, gbc);

    // Step 2: Select match for that team
    gbc.gridx = 0; gbc.gridy = 1;
    dialog.add(new JLabel("Select Match:"), gbc);
    JComboBox<Match> matchBox = new JComboBox<>();
    gbc.gridx = 1;
    dialog.add(matchBox, gbc);

    // Update match dropdown when team changes
    teamBox.addActionListener(e -> {
        Team selectedTeam = (Team) teamBox.getSelectedItem();
        if (selectedTeam != null) {
            try {
                List<Match> matches = matchDAO.lookupTeamMatches(selectedTeam.getTeamId());
                matchBox.removeAllItems();
                for (Match m : matches) {
                    matchBox.addItem(m);
                }
            } catch (Exception ex) {
                logArea.append("Error loading matches: " + ex.getMessage() + "\n");
            }
        }
    });

    // Home score input
    gbc.gridx = 0; gbc.gridy = 2;
    dialog.add(new JLabel("Home Score:"), gbc);
    JTextField homeScoreField = new JTextField(3);
    gbc.gridx = 1;
    dialog.add(homeScoreField, gbc);

    // Away score input
    gbc.gridx = 0; gbc.gridy = 3;
    dialog.add(new JLabel("Away Score:"), gbc);
    JTextField awayScoreField = new JTextField(3);
    gbc.gridx = 1;
    dialog.add(awayScoreField, gbc);

    // Update button
    gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
    JButton updateBtn = new JButton("Update Match");
    dialog.add(updateBtn, gbc);

    updateBtn.addActionListener(e -> {
        try {
            Match selectedMatch = (Match) matchBox.getSelectedItem();
            if (selectedMatch == null) return;

            int homeScore = Integer.parseInt(homeScoreField.getText());
            int awayScore = Integer.parseInt(awayScoreField.getText());

            matchDAO.editMatch(selectedMatch.getMatchId(), homeScore, awayScore);
            logArea.append("Edited match: " + selectedMatch + " -> " + homeScore + "-" + awayScore + "\n");
            refreshStandingsTable();
            dialog.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
        }
    });

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}



   private void openDeleteMatchWindow() {
    logArea.append("Delete Match clicked.\n");

    JDialog dialog = new JDialog(this, "Delete Match", true);
    dialog.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5,5,5,5);

    // Select team
    gbc.gridx = 0; gbc.gridy = 0;
    dialog.add(new JLabel("Select Team:"), gbc);
    List<Team> teams = teamDAO.lookupStandings();
    JComboBox<Team> teamBox = new JComboBox<>(teams.toArray(new Team[0]));
    gbc.gridx = 1;
    dialog.add(teamBox, gbc);

    // Select match for that team
    gbc.gridx = 0; gbc.gridy = 1;
    dialog.add(new JLabel("Select Match:"), gbc);
    JComboBox<Match> matchBox = new JComboBox<>();
    gbc.gridx = 1;
    dialog.add(matchBox, gbc);

    // Update match dropdown when team changes
    teamBox.addActionListener(e -> {
        Team selectedTeam = (Team) teamBox.getSelectedItem();
        if (selectedTeam != null) {
            try {
                List<Match> matches = matchDAO.lookupTeamMatches(selectedTeam.getTeamId());
                matchBox.removeAllItems();
                for (Match m : matches) {
                    matchBox.addItem(m);
                }
            } catch (Exception ex) {
                logArea.append("Error loading matches: " + ex.getMessage() + "\n");
            }
        }
    });

    // Delete button
    gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
    JButton deleteBtn = new JButton("Delete Match");
    dialog.add(deleteBtn, gbc);

    deleteBtn.addActionListener(e -> {
        try {
            Match selectedMatch = (Match) matchBox.getSelectedItem();
            if (selectedMatch == null) return;

            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Are you sure you want to delete this match?\n" + selectedMatch,
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                matchDAO.deleteMatch(selectedMatch.getMatchId());
                logArea.append("Deleted match: " + selectedMatch + "\n");
                refreshStandingsTable();
                dialog.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
        }
    });

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}


    private void openLookupTeamWindow() {
    logArea.append("Lookup Team Matches clicked.\n");

    JDialog dialog = new JDialog(this, "Lookup Team Matches", true);
    dialog.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5,5,5,5);

    // Dropdown to select team
    gbc.gridx = 0; gbc.gridy = 0;
    dialog.add(new JLabel("Select Team:"), gbc);
    List<Team> teams = teamDAO.lookupStandings();
    JComboBox<Team> teamBox = new JComboBox<>(teams.toArray(new Team[0]));
    gbc.gridx = 1;
    dialog.add(teamBox, gbc);

    // Lookup button
    gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
    JButton lookupBtn = new JButton("Show Matches");
    dialog.add(lookupBtn, gbc);

    lookupBtn.addActionListener(e -> {
        try {
            Team selectedTeam = (Team) teamBox.getSelectedItem();
            List<Match> matches = matchDAO.lookupTeamMatches(selectedTeam.getTeamId());

            // Show matches in a JTable inside a new dialog
            JDialog resultsDialog = new JDialog(dialog, selectedTeam.getTeamName() + " Matches", true);
            DefaultTableModel model = new DefaultTableModel(new String[]{"Match ID", "Home", "Away", "Home Score", "Away Score"}, 0);
            JTable table = new JTable(model);
            for (Match m : matches) {
                model.addRow(new Object[]{m.getMatchId(), m.getHomeTeamId(), m.getAwayTeamId(), m.getHomeScore(), m.getAwayScore()});
            }
            resultsDialog.add(new JScrollPane(table));
            resultsDialog.pack();
            resultsDialog.setLocationRelativeTo(dialog);
            resultsDialog.setVisible(true);

            logArea.append("Displayed matches for team: " + selectedTeam.getTeamName() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
        }
    });

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
