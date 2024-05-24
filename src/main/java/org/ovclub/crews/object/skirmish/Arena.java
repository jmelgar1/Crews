package org.ovclub.crews.object.skirmish;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.ovclub.crews.managers.file.ConfigManager;
import org.ovclub.crews.object.Crew;
import org.ovclub.crews.utilities.SoundUtilities;
import org.ovclub.crews.utilities.UnicodeCharacters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Arena {
    UUID arenaId;
    Scoreboard pointScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    Objective objective = pointScoreboard.registerNewObjective("skirmish", "dummy",
        Component.text(UnicodeCharacters.crews).color(UnicodeCharacters.logo_color)
            .append(Component.text("SKIRMISH ").color(UnicodeCharacters.description_color).decorate(TextDecoration.BOLD)
                .append(Component.text(UnicodeCharacters.crews).color(UnicodeCharacters.logo_color))));
    private final Location center;
    private final int radius;
    private final World world;
    private final Skirmish skirmish;
    private boolean hasBeenTeleported;
    private boolean isInCountdown;
    //private int gameTime = 600;
    private int gameTime = 180;
    private final HashMap<UUID, Location> playerReturnPoints = new HashMap<>();

    private final Map<Integer, Integer> kFactors;

    public Arena(World world, Location center, int radius, Skirmish skirmish, boolean isInCountdown) {
        this.arenaId = UUID.randomUUID();
        this.world = world;
        this.center = center;
        this.radius = radius;
        this.skirmish = skirmish;
        this.isInCountdown = isInCountdown;

        kFactors = new HashMap<>();
        kFactors.put(1000, 150);
        kFactors.put(2000, 100);
        kFactors.put(3000, 66);
        kFactors.put(4000, 44);
        kFactors.put(5000, 29);
        kFactors.put(6000, 19);
        kFactors.put(7000, 12);
    }

    public UUID getArenaId() { return arenaId; }

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
        Score teamName1 = objective.getScore(ChatColor.BLUE + matchup.getATeam().getCrew().getName().toUpperCase());
        teamName1.setScore(10);
        Score teamScore1 = objective.getScore(ChatColor.BLUE + "• " + ChatColor.WHITE + "Points: " + ChatColor.GOLD + skirmish.getATeamScore() + " ");
        teamScore1.setScore(9);

        objective.getScore("  ").setScore(8);

        //team red
        Score teamName2 = objective.getScore(ChatColor.RED + matchup.getBTeam().getCrew().getName().toUpperCase());
        teamName2.setScore(7);
        Score teamScore2 = objective.getScore(ChatColor.RED + "• " + ChatColor.WHITE + "Points: " + ChatColor.GOLD + skirmish.getBTeamScore());
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

    public void updateTeamScore(boolean isATeam, int newScore) {
        String teamPrefix = isATeam ? ChatColor.BLUE + "• " : ChatColor.RED + "• ";
        int scoreLine = isATeam ? 9 : 6;
        String scoreText = teamPrefix + ChatColor.WHITE + "Points: " + ChatColor.GOLD + newScore;

        resetScoreAtLine(scoreLine);
        Score score = objective.getScore(scoreText);
        score.setScore(scoreLine);
    }

    public void updateOpponentTeamScore(SkirmishTeam team) {
       if(team.equals(getMatchup().getATeam())) {
           int newScore = this.skirmish.getBTeamScore() + 1;
           updateTeamScore(false, newScore);
           this.skirmish.setBTeamScore(newScore);
       } else {
           int newScore = this.skirmish.getATeamScore() + 1;
           updateTeamScore(true, newScore);
           this.skirmish.setATeamScore(newScore);
       }
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
        } else {
            team.unregister();
            team = scoreboard.registerNewTeam(teamName);
        }
        team.setDisplayName(color + " Test");
        team.setColor(color);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        team.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.NEVER);
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
        SkirmishTeam team1 = matchup.getATeam();
        SkirmishTeam team2 = matchup.getBTeam();

        String pUUID = String.valueOf(p.getUniqueId());
        if(team1.getPlayers().contains(pUUID)) {
            return team1;
        } else {
            return team2;
        }
    }

    public void determineOutcome() {
        int teamA_score = this.skirmish.getATeamScore();
        int teamB_score = this.skirmish.getBTeamScore();
        SkirmishTeam teamA = this.skirmish.getMatchup().getATeam();
        SkirmishTeam teamB = this.skirmish.getMatchup().getBTeam();
        Crew crewA =  teamA.getCrew();
        Crew crewB =  teamB.getCrew();

        int result = Integer.compare(teamA_score, teamB_score);
        int teamA_rating = teamA.getCrew().getRating();
        int teamB_rating = teamB.getCrew().getRating();
        Rating rating = new Rating(teamA.getCrew().getRating(), teamB.getCrew().getRating(), result, kFactors);
        Map<String, Double> newRatings = rating.getNewRatings();
        int teamA_newRating= newRatings.get("a").intValue();
        int teamB_newRating= newRatings.get("b").intValue();

        if(teamA_score > teamB_score) {
            crewA.addSkirmishWins(1);
            crewB.addSkirmishLosses(1);
            Bukkit.broadcast(ConfigManager.SKIRMISH_ENDED
                .replaceText(builder -> builder.matchLiteral("{winner}").replacement(crewA.getName()))
                .replaceText(builder -> builder.matchLiteral("{team1}").replacement(crewA.getName()))
                .replaceText(builder -> builder.matchLiteral("{team2}").replacement(crewB.getName()))
                .replaceText(builder -> builder.matchLiteral("{points1}").replacement(Component.text(teamA_score).color(NamedTextColor.DARK_GREEN)))
                .replaceText(builder -> builder.matchLiteral("{points2}").replacement(Component.text(teamB_score).color(NamedTextColor.DARK_RED))));

            crewA.broadcast(ConfigManager.GAINED_RATING
                .replaceText(builder -> builder.matchLiteral("{oldRating}").replacement(String.valueOf(teamA_rating)))
                .replaceText(builder -> builder.matchLiteral("{change}").replacement(String.valueOf(teamA_newRating - teamA_rating)))
                .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(teamA_newRating))));
            crewB.broadcast(ConfigManager.LOST_RATING
                .replaceText(builder -> builder.matchLiteral("{oldRating}").replacement(String.valueOf(teamB_rating)))
                .replaceText(builder -> builder.matchLiteral("{change}").replacement(String.valueOf(teamB_rating - teamB_newRating)))
                .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(teamB_newRating))));

            SoundUtilities.playSoundToSpecificPlayers(teamA.getPlayers(), SoundUtilities.skirmishVictorySound, 1.0F, 1.0F);
            SoundUtilities.playSoundToSpecificPlayers(teamB.getPlayers(), SoundUtilities.skirmishDefeatSound, 1.0F, 1.0F);
        } else if(teamB_score > teamA_score) {
            crewB.addSkirmishWins(1);
            crewA.addSkirmishLosses(1);
            Bukkit.broadcast(ConfigManager.SKIRMISH_ENDED
                .replaceText(builder -> builder.matchLiteral("{winner}").replacement(crewB.getName()))
                .replaceText(builder -> builder.matchLiteral("{team1}").replacement(crewA.getName()))
                .replaceText(builder -> builder.matchLiteral("{team2}").replacement(crewB.getName()))
                .replaceText(builder -> builder.matchLiteral("{points1}").replacement(Component.text(teamA_score).color(NamedTextColor.DARK_RED)))
                .replaceText(builder -> builder.matchLiteral("{points2}").replacement(Component.text(teamB_score).color(NamedTextColor.DARK_GREEN))));

            crewB.broadcast(ConfigManager.GAINED_RATING
                .replaceText(builder -> builder.matchLiteral("{oldRating}").replacement(String.valueOf(teamB_rating)))
                .replaceText(builder -> builder.matchLiteral("{change}").replacement(String.valueOf(teamB_newRating - teamB_rating)))
                .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(teamB_newRating))));
            crewA.broadcast(ConfigManager.LOST_RATING
                .replaceText(builder -> builder.matchLiteral("{oldRating}").replacement(String.valueOf(teamA_rating)))
                .replaceText(builder -> builder.matchLiteral("{change}").replacement(String.valueOf(teamA_rating - teamA_newRating)))
                .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(teamA_newRating))));

            SoundUtilities.playSoundToSpecificPlayers(teamB.getPlayers(), SoundUtilities.skirmishVictorySound, 1.0F, 1.0F);
            SoundUtilities.playSoundToSpecificPlayers(teamA.getPlayers(), SoundUtilities.skirmishDefeatSound, 1.0F, 1.0F);
        } else {
            crewA.addSkirmishDraws(1);
            crewB.addSkirmishDraws(1);
            Bukkit.broadcast(ConfigManager.SKIRMISH_DRAW
                .replaceText(builder -> builder.matchLiteral("{team1}").replacement(crewA.getName()))
                .replaceText(builder -> builder.matchLiteral("{team2}").replacement(crewB.getName()))
                .replaceText(builder -> builder.matchLiteral("{points1}").replacement(String.valueOf(teamA_score)))
                .replaceText(builder -> builder.matchLiteral("{points2}").replacement(String.valueOf(teamB_score))));

            SoundUtilities.playSoundToSpecificPlayers(this.skirmish.getMatchup().getParticipants(), SoundUtilities.skirmishDrawSound, 0.5F, 1.0F);

            if(teamA_newRating - teamA_rating > teamB_newRating - teamB_rating) {
                crewA.broadcast(ConfigManager.GAINED_RATING
                    .replaceText(builder -> builder.matchLiteral("{oldRating}").replacement(String.valueOf(teamA_rating)))
                    .replaceText(builder -> builder.matchLiteral("{change}").replacement(String.valueOf(teamA_newRating - teamA_rating)))
                    .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(teamA_newRating))));
                crewB.broadcast(ConfigManager.LOST_RATING
                    .replaceText(builder -> builder.matchLiteral("{oldRating}").replacement(String.valueOf(teamB_rating)))
                    .replaceText(builder -> builder.matchLiteral("{change}").replacement(String.valueOf(teamB_rating - teamB_newRating)))
                    .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(teamB_newRating))));
            } else if(teamB_newRating - teamB_rating > teamA_newRating - teamA_rating){
                crewB.broadcast(ConfigManager.GAINED_RATING
                    .replaceText(builder -> builder.matchLiteral("{oldRating}").replacement(String.valueOf(teamB_rating)))
                    .replaceText(builder -> builder.matchLiteral("{change}").replacement(String.valueOf(teamB_newRating - teamB_rating)))
                    .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(teamB_newRating))));
                crewA.broadcast(ConfigManager.LOST_RATING
                    .replaceText(builder -> builder.matchLiteral("{oldRating}").replacement(String.valueOf(teamA_rating)))
                    .replaceText(builder -> builder.matchLiteral("{change}").replacement(String.valueOf(teamA_rating - teamA_newRating)))
                    .replaceText(builder -> builder.matchLiteral("{total}").replacement(String.valueOf(teamA_newRating))));
            } else {
                crewA.broadcast(ConfigManager.NO_CHANGE_IN_RATING);
                crewB.broadcast(ConfigManager.NO_CHANGE_IN_RATING);
            }
        }

        teamA.getCrew().setRating(newRatings.get("a").intValue());
        teamB.getCrew().setRating(newRatings.get("b").intValue());
    }
}
