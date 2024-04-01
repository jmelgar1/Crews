package org.diffvanilla.crews.commands;

import org.bukkit.entity.Player;
import org.diffvanilla.crews.Crews;
import org.diffvanilla.crews.exceptions.NotInCrew;

public interface SubCommand {

  abstract String getDescription();
  abstract String getSyntax();
  abstract String getPermission();
  abstract void perform(Player p, String[] args, Crews plugin) throws NotInCrew;
}
