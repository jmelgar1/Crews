package org.ovclub.crews.commands;

import org.bukkit.entity.Player;
import org.ovclub.crews.Crews;
import org.ovclub.crews.exceptions.NotInCrew;

public interface SubCommand {

  abstract String getDescription();
  abstract String getSyntax();
  abstract String getPermission();
  abstract void perform(Player p, String[] args, Crews plugin) throws NotInCrew;
}
