package org.ovclub.crews.object.skirmish;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.ovclub.crews.managers.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.utilities.RatingUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

import java.util.HashMap;
import java.util.UUID;

public class Arena {
    Scoreboard pointScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    Objective objective = pointScoreboard.registerNewObjective("skirmish", "dummy",
        Component.text(UnicodeCharacters.crews).color(UnicodeCharacters.logo_color)
            .append(Component.text("SKIRMISH ").color(UnicodeCharacters.description_color).decorate(TextDecoration.BOLD)
                .append(Component.text(UnicodeCharacters.crews).color(UnicodeCharacters.logo_color))));
    private Location center;
    private final int radius;
    private World world;
    private Skirmish skirmish;
    private boolean hasBeenTeleported;
    private boolean isInCountdown;
    //private int gameTime = 600;
    private int gameTime = 60;
    private final HashMap<UUID, Location> playerReturnPoints = new HashMap<>();

    public Arena(World world, Location center, int radius, Skirmish skirmish, boolean isInCountdown) {
        this.world = world;
        this.center = center;
        this.radius = radius;
        this.skirmish = skirmish;
        this.isInCountdown = isInCountdown;
    }

    public Location getCenter() {
        return center;
    }

    public World getWorld() {
        return world;
    }

    public int getRadius() {
        return radius;
    }

    public Skirmish getSkirmish() { return this.skirmish; }

    public SkirmishMatchup getMatchup() { return this.skirmish.getMatchup(); }

    public int getGameTime() {return this.gameTime;}

    public boolean getHasBeenTeleported() {
        return hasBeenTeleported;
    }

    public void setHasBeenTeleported(boolean value) {
        this.hasBeenTeleported = value;
    }

    public boolean getIsInCountdown() {
        return this.isInCountdown;
    }

    public void setIsInCountdown(boolean value) {
        this.isInCountdown = value;
    }

    public void setGameTime(int value) {
        this.gameTime = value;
    }

    public void addPlayerReturnPoint(UUID uuid, Location loc){this.playerReturnPoints.put(uuid, loc);}

    public HashMap<UUID, Location> getPlayerReturnPoint(){return this.playerReturnPoints;}

    public void setupScoreboardForAllPlayers() {
        for (String uuid : this.skirmish.getMatchup().getParticipants()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if(p != null && p.isOnline()) {
                setupPlayerScoreboard(p, this.skirmish.getMatchup());
            }
        }
    }

    public void setupPlayerScoreboard(Player player, SkirmishMatchup matchup) {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(" ").setScore(11);

        //team blue
        Score teamName1 = objective.getScore(ChatColor.BLUE + matchup.getBlueTeam().getCrew().getName().toUpperCase());
        teamName1.setScore(10);
        Score teamScore1 = objective.getScore(ChatColor.BLUE + "• " + ChatColor.WHITE + "Points: " + ChatColor.GOLD + skirmish.getBlueTeamScore() + " ");
        teamScore1.setScore(9);

        objective.getScore("  ").setScore(8);

        //team red
        Score teamName2 = objective.getScore(ChatColor.RED + matchup.getRedTeam().getCrew().getName().toUpperCase());
        teamName2.setScore(7);
        Score teamScore2 = objective.getScore(ChatColor.RED + "• " + ChatColor.WHITE + "Points: " + ChatColor.GOLD + skirmish.getRedTeamScore());
        teamScore2.setScore(6);

        objective.getScore("   ").setScore(5);

        //time left
        Score timeLeftTitle = objective.getScore(ChatColor.YELLOW + "Time Left:");
        timeLeftTitle.setScore(4);
        Score timeLeft = objective.getScore(ChatColor.GRAY + "10:00");
        timeLeft.setScore(3);

        objective.getScore("    ").setScore(2);

        //server ip
        Score serverIp = objective.getScore(ChatColor.DARK_AQUA + "play.ovclub.gg");
        serverIp.setScore(1);

        player.setScoreboard(pointScoreboard);
    }

    public void removePlayerScoreboard() {
        for (String uuid : this.skirmish.getMatchup().getParticipants()) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if(p != null && p.isOnline()) {
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard blankScoreboard = manager.getNewScoreboard();
                p.setScoreboard(blankScoreboard);
            }
        }
    }

    public void updateTimeOnScoreboard(Scoreboard scoreboard, int gameTime) {
        if (objective != null) {
            scoreboard.getEntries().forEach(entry -> {
                if (entry.startsWith(ChatColor.GRAY.toString())) {
                    scoreboard.resetScores(entry);
                }
            });
            objective.getScore(ChatColor.GRAY + formatTime(gameTime)).setScore(3);
        }
    }

    public void updateTeamScore(boolean isBlueTeam, int newScore) {
        String teamPrefix = isBlueTeam ? ChatColor.BLUE + "• " : ChatColor.RED + "• ";
        int scoreLine = isBlueTeam ? 9 : 6;
        String scoreText = teamPrefix + ChatColor.WHITE + " Points: " + ChatColor.GOLD + newScore;

        resetScoreAtLine(scoreLine);
        Score score = objective.getScore(scoreText);
        score.setScore(scoreLine);
    }

    private void resetScoreAtLine(int line) {
        pointScoreboard.getEntries().forEach(entry -> {
            Score score = objective.getScore(entry);
            if (score.getScore() == line) {
                pointScoreboard.resetScores(entry);
            }
        });
    }

    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void setTeamColor(Player player, ChatColor color) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Team existingTeam : scoreboard.getTeams()) {
            if (existingTeam.hasEntry(player.getName())) {
                existingTeam.removeEntry(player.getName());
            }
        }
        String teamName = color.name() + "Team";
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setDisplayName(color + "Test");
            team.setColor(color);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            team.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.NEVER);
        } else if (team.getColor() != color) {
            team.setDisplayName(color + "Test");
            team.setColor(color);
        }
        team.addEntry(player.getName());
    }
    public void removePlayerFromTeams(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }
    public SkirmishTeam getPlayerTeam(Player p) {
        SkirmishMatchup matchup = this.skirmish.getMatchup();
        SkirmishTeam team1 = matchup.getBlueTeam();
        SkirmishTeam team2 = matchup.getRedTeam();

        String pUUID = String.valueOf(p.getUniqueId());
        if(team1.getPlayers().contains(pUUID)) {
            return team1;
        } else {
            return team2;
        }
    }

    public void determineOutcome() {
        int blueScore = this.skirmish.getBlueTeamScore();
        int redScore = this.skirmish.getRedTeamScore();
        SkirmishTeam blueTeam = this.skirmish.getMatchup().getBlueTeam();
        SkirmishTeam redTeam = this.skirmish.getMatchup().getRedTeam();
        Crew blueCrew =  blueTeam.getCrew();
        Crew redCrew =  redTeam.getCrew();

        if(blueScore > redScore) {
            blueCrew.addSkirmishWins(1);
            redCrew.addSkirmishLosses(1);
            Bukkit.broadcast(ConfigManager.SKIRMISH_ENDED
                .replaceText(builder -> builder.matchLiteral("{winner}").replacement(blueCrew.getName()))
                .replaceText(builder -> builder.matchLiteral("{loser}").replacement(redCrew.getName()))
                .replaceText(builder -> builder.matchLiteral("{points1}").replacement(String.valueOf(blueScore)))
                .replaceText(builder -> builder.matchLiteral("{points2}").replacement(String.valueOf(redScore))));

            org.ovclub.crews.utilities.RatingUtilities.updateRatings();
        } else if(redScore > blueScore) {
            redCrew.addSkirmishWins(1);
            blueCrew.addSkirmishLosses(1);
            Bukkit.broadcast(ConfigManager.SKIRMISH_ENDED
                .replaceText(builder -> builder.matchLiteral("{winner}").replacement(redCrew.getName()))
                .replaceText(builder -> builder.matchLiteral("{loser}").replacement(blueCrew.getName()))
                .replaceText(builder -> builder.matchLiteral("{points1}").replacement(String.valueOf(redScore)))
                .replaceText(builder -> builder.matchLiteral("{points2}").replacement(String.valueOf(blueScore))));
        } else {
            blueCrew.addSkirmishDraws(1);
            redCrew.addSkirmishDraws(1);
            Bukkit.broadcast(ConfigManager.SKIRMISH_DRAW
                .replaceText(builder -> builder.matchLiteral("{team1}").replacement(blueCrew.getName()))
                .replaceText(builder -> builder.matchLiteral("{team2}").replacement(redCrew.getName()))
                .replaceText(builder -> builder.matchLiteral("{points1}").replacement(String.valueOf(blueScore)))
                .replaceText(builder -> builder.matchLiteral("{points2}").replacement(String.valueOf(redScore))));
        }
    }

    private void RatingUtilities.updateRatings(arena.getMatchup().);
}
