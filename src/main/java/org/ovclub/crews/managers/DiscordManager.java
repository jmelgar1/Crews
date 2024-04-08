//package org.diffvanilla.crews.managers;
//
//import net.dv8tion.jda.api.Permission;
//import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.channel.concrete.Category;
//import net.md_5.bungee.api.ChatColor;
//import org.bukkit.entity.Player;
//import org.diffvanilla.crews.Crews;
//import org.diffvanilla.crews.utilities.ChatUtilities;
//
//public class DiscordManager {
//
//    //Crews instance
//    protected Crews crewsClass = Crews.getInstance();
//    ChatUtilities chatUtil = new ChatUtilities();
//    UpgradeManager upgradeManager = new UpgradeManager();
//    CrewManager crewManager = new CrewManager();
//
//   public DiscordManager(String playerCrew, Player p, String DiscordName) {
//       String guildId = crewsClass.getDiscordSettings().getString("guild_id");
//       String categoryId = crewsClass.getDiscordSettings().getString("category_id");
//
//       int purchasePrice = crewsClass.getPrices().getConfigurationSection("upgrades").getInt("discord");
//
//       if (guildId != null && categoryId != null) {
//           Guild guild = crewsClass.getJda().getGuildById(guildId);
//
//           if (guild != null) {
//               Category category = guild.getCategoryById(categoryId);
//
//               if (category != null) {
//                   category.createTextChannel(playerCrew + " - Text").queue(channel -> {
//
//                       guild.retrieveMembersByPrefix(DiscordName, 100).onSuccess(members -> {
//                           Member member = members.stream()
//                                   .filter(m -> m.getUser().getEffectiveName().equalsIgnoreCase(DiscordName))
//                                   .findFirst().orElse(null);
//                           if(member != null){
//                               channel.upsertPermissionOverride(guild.getPublicRole()).setDenied(Permission.VIEW_CHANNEL).queue();
//                               channel.upsertPermissionOverride(member).grant(Permission.VIEW_CHANNEL,
//                                       Permission.MESSAGE_SEND, Permission.CREATE_INSTANT_INVITE, Permission.KICK_MEMBERS, Permission.MESSAGE_ADD_REACTION,
//                                       Permission.MESSAGE_HISTORY, Permission.MANAGE_PERMISSIONS).queue();
//
//                               channel.sendMessage(rulesMessage(playerCrew, member)).queue();
//                               crewManager.removeFromVault(playerCrew, purchasePrice/2, p);
//                               chatUtil.UpgradeSuccessful(playerCrew, "discord-text");
//                               upgradeManager.editUpgrade(playerCrew, "discord", true);
//                               p.sendMessage(chatUtil.successIcon + " Text channel created!" + ChatColor.GREEN + " Location: Server Discord > crew Channels > " + playerCrew + " - Text");
//                           } else {
//                               channel.delete().queue();
//                               upgradeManager.editUpgrade(playerCrew, "discord", false);
//                               p.sendMessage(chatUtil.errorIcon + "Discord username not found.");
//                           }
//                       });
//
//                   }, throwable -> {
//                       p.sendMessage("Error creating channel: " + throwable.getMessage());
//                   });
//
//                   category.createVoiceChannel(playerCrew + " - Voice").queue(channel -> {
//
//                       guild.retrieveMembersByPrefix(DiscordName, 100).onSuccess(members -> {
//                            Member member = members.stream()
//                                    .filter(m -> m.getUser().getEffectiveName().equalsIgnoreCase(DiscordName))
//                                    .findFirst().orElse(null);
//                            if(member != null){
//                                channel.upsertPermissionOverride(guild.getPublicRole()).setDenied(Permission.VIEW_CHANNEL).queue();
//                                channel.upsertPermissionOverride(member).grant(Permission.VIEW_CHANNEL,
//                                        Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.VOICE_DEAF_OTHERS, Permission.VOICE_MUTE_OTHERS,
//                                        Permission.MANAGE_PERMISSIONS).queue();
//
//                                channel.sendMessage(rulesMessage(playerCrew, member)).queue();
//                                crewManager.removeFromVault(playerCrew, purchasePrice/2, p);
//                                chatUtil.UpgradeSuccessful(playerCrew, "discord-voice");
//                                upgradeManager.editUpgrade(playerCrew, "discord", true);
//                                p.sendMessage(chatUtil.successIcon + " Voice channel created!" + ChatColor.GREEN + " Location: Server Discord > crew Channels > " + playerCrew + " - Voice");
//                            } else {
//                                channel.delete().queue();
//                                upgradeManager.editUpgrade(playerCrew, "discord", false);
//                                p.sendMessage(chatUtil.errorIcon + "Discord username not found.");
//                            }
//                       });
//
//                   }, throwable -> {
//                       p.sendMessage("Error creating channel: " + throwable.getMessage() + ". Report this error.");
//                   });
//
//                   p.closeInventory();
//               } else {
//                   p.sendMessage("Category not found. Report this error.");
//               }
//           } else {
//               p.sendMessage("Server ID not found. Report this error.");
//           }
//       } else {
//           p.sendMessage("Server or CategoryId not found. Report this error.");
//       }
//   }
//
//   private String rulesMessage(String playerCrew, Member memberOwner){
//
//       String[] messageLines = {
//               "**" + playerCrew + " Channel Disclaimer**",
//               "",
//               "_Channel Owner:_ " + memberOwner.getAsMention(),
//               "",
//               "_1._ You as the chief/channel owner are responsible for this channel. You can kick/ban/invite whoever as you wish.",
//               "_2._ You are liable for any damage done in-game through inviting the wrong people to this channel. Be careful please.",
//               "_3._ All server discord rules apply to this channel and the server has the right to shut down the channel for any reason.",
//               "_4._ Use common sense and use this channel responsibly.",
//               "",
//               "_Reminder:_ You as the channel owner have full administrative permissions for this channel. You are allowed to transfer ownership to whomever" +
//                       " you want."
//       };
//
//       return String.join("\n", messageLines);
//    }
//}
