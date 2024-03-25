package org.diffvanilla.crews.commands;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.exceptions.NotInCrew;
import org.diffvanilla.crews.managers.CrewManager;
import org.diffvanilla.crews.managers.UpgradeManager;
import org.diffvanilla.crews.managers.WarpManager;
import org.diffvanilla.crews.utilities.ChatUtilities;
import org.diffvanilla.crews.utilities.JsonUtilities;
import org.diffvanilla.crews.utilities.UnicodeCharacters;

public interface SubCommand {

  abstract String getDescription();
  abstract String getSyntax();
  abstract String getPermission();
  abstract void perform(Player p, String[] args, Crews plugin) throws NotInCrew;

//	public Crews crewsClass = Crews.getInstance();
//	public CrewManager crewManager = new CrewManager();
//	public ChatUtilities chatUtil = new ChatUtilities();
//	public UnicodeCharacters unicode = new UnicodeCharacters();
//	public WarpManager warpManager = new WarpManager();
//	public UpgradeManager upgradeManager = new UpgradeManager();
//	public JsonUtilities json = new JsonUtilities();
//
//	public abstract String getName();
//
//	public abstract String getDescription();
//
//	public abstract void perform(Player p, String args[]);
}
