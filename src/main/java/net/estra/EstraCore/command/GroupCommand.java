package net.estra.EstraCore.command;

import net.estra.EstraCore.EstraCorePlugin;
import net.estra.EstraCore.model.Group;
import net.estra.EstraCore.model.Rank;
import net.estra.EstraCore.model.chat.GlobalChat;
import net.estra.EstraCore.model.chat.GroupChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vg.civcraft.mc.namelayer.GroupManager;
import vg.civcraft.mc.namelayer.NameAPI;
import vg.civcraft.mc.namelayer.RunnableOnGroup;

import java.util.*;
import java.util.stream.Collectors;

public class GroupCommand implements CommandExecutor {

    List<UUID> waitingConfirms = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "This command can only be used in-game!");
            return true;
        }
        Player player = (Player) commandSender;

        if(args.length < 1 || args[0].equals("help")) {
            //Help List if no arguments provided, or if first arg was "help"
            player.sendMessage(ChatColor.DARK_GRAY + "Estra" + ChatColor.RED + "Core\n"
                    + ChatColor.YELLOW + "create " + ChatColor.DARK_GRAY + "- Creates a group.\n"
                    + ChatColor.YELLOW + "delete " + ChatColor.DARK_GRAY + "- Deletes a group.\n"
                    + ChatColor.YELLOW + "invite " + ChatColor.DARK_GRAY + "- Invites a player to a group.\n"
                    + ChatColor.YELLOW + "join " + ChatColor.DARK_GRAY + "- Joins a group.\n"
                    + ChatColor.YELLOW + "deny " + ChatColor.DARK_GRAY + "- Denies an invite to a group.\n"
                    + ChatColor.YELLOW + "chat " + ChatColor.DARK_GRAY + "- Chats to a group.\n"
                    + ChatColor.YELLOW + "promote " + ChatColor.DARK_GRAY + "- Promotes a member within a group.\n"
                    + ChatColor.YELLOW + "demote " + ChatColor.DARK_GRAY + "- Demotes a member within a group.\n"
                    + ChatColor.YELLOW + "kick " + ChatColor.DARK_GRAY + "- Kicks a member from a group.\n"

            );
            return true;
        }
        //We fetch the group back here that way we can just reference whenever needed.
        Group group = EstraCorePlugin.instance.getGroupManager().getPlayerGroup(player);
        //We use a switch statement since it honestly just looks so much more clean.
        switch (args[0].toLowerCase(Locale.ROOT)) {

            //Should have at least 2 args provided.
            case("create"):
                if(args.length < 2) { player.sendMessage(ChatColor.RED + "You need to specify a name for the group. /group create <name>"); return true; }
                //if player is already in a group, we don't want to reference it.
                if(group != null) { player.sendMessage(ChatColor.RED + "You are already in a group!"); return true; }
                String groupName = args[1];
                vg.civcraft.mc.namelayer.group.Group g = new vg.civcraft.mc.namelayer.group.Group(groupName, NameAPI.getUUID(player.getName()), false, null, -1);
                NameAPI.getGroupManager().createGroupAsync(g, new RunnableOnGroup() {
                    @Override
                    public void run() {
                        Player p = null;
                        p = Bukkit.getPlayer(NameAPI.getUUID(player.getName()));
                        vg.civcraft.mc.namelayer.group.Group g = getGroup();
                        if (p != null) {
                            if (g.getGroupId() == -1) { // failure
                                p.sendMessage(ChatColor.RED + "That group is already taken or creation failed.");
                                return;
                            }
                            p.sendMessage(ChatColor.GREEN + "Namelayer group " + g.getName() + " was successfully created.");
                        } else {
                            EstraCorePlugin.instance.getLogger().info("Group " + g.getName() + " creation is complete, resulting in group id: "
                                    + g.getGroupId());
                        }
                    }
                    }, false);
                Bukkit.getScheduler().runTaskLater(EstraCorePlugin.instance, new Runnable() {
                    @Override
                    public void run() {
                        NameAPI.getGroupManager().getGroup(groupName).setDefaultGroup(player.getUniqueId());
                    }
                }, 40);
                EstraCorePlugin.instance.getGroupManager().createGroup(groupName, player);
                player.sendMessage(ChatColor.GREEN + "Successfully created group " + groupName + ".");
                return true;
            case("delete"):
                if(group == null) { player.sendMessage(ChatColor.RED + "You aren't in a group."); return true; }
                Rank rank0 = group.getMemberRank(player);
                if(rank0 != Rank.OWNER) { player.sendMessage(ChatColor.RED + "You must be the owner of a group to delete the group."); return true; }
                player.sendMessage(ChatColor.RED + "If you really want to delete this group, type " + ChatColor.GREEN + "/group confirm "
                + ChatColor.RED + " in " + ChatColor.YELLOW + "15 Seconds");
                waitingConfirms.add(player.getUniqueId());
                Bukkit.getScheduler().runTaskLater(EstraCorePlugin.instance, new Runnable() {
                    @Override
                    public void run() {
                        waitingConfirms.remove(player.getUniqueId());
                    }
                }, 300);
                return true;
            case("confirm"):
                if(group == null) { player.sendMessage(ChatColor.RED + "You aren't in a group."); return true; }
                Rank rank = group.getMemberRank(player);
                if(rank != Rank.OWNER) { player.sendMessage(ChatColor.RED + "You must be the owner of a group to delete the group."); return true; }
                //Nothing else to do but have the group be removed from the group manager.
                if(!waitingConfirms.contains(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "There was no pending group deletion request.");
                    return true;
                }
                EstraCorePlugin.instance.getGroupManager().removeGroup(group);
                NameAPI.getGroupManager().deleteGroup(group.getName());
                group.sendGroupMessage("SYSTEM", ChatColor.RED + "This group has been disbanded.");
                player.sendMessage(ChatColor.GREEN + "Group was deleted.");
                return true;
            case("invite"):
                if(args.length < 2) { player.sendMessage(ChatColor.RED + "You need to specify a player to invite. /group invite <player>"); return true; }
                if(group == null) { player.sendMessage(ChatColor.RED + "You aren't in a group."); return true; }
                Rank rank1 = group.getMemberRank(player);
                if(rank1 != Rank.OWNER && rank1 != Rank.OFFICER) { player.sendMessage(ChatColor.RED + "You do not have permission to invite players!"); return true; }
                Player invitee = Bukkit.getPlayer(args[1]);
                if(invitee == null) { player.sendMessage(ChatColor.RED + "That player doesn't exist!"); return true; }
                if(EstraCorePlugin.instance.getGroupManager().isPlayerInGroup(invitee)) { player.sendMessage(ChatColor.RED + "That player is already in a group."); return true; }
                boolean work = EstraCorePlugin.instance.getGroupManager().putPendingInvite(invitee, group);
                if(!work) { player.sendMessage(ChatColor.RED + "That player already had a pending invite."); return true; }
                player.sendMessage(ChatColor.GREEN + "Successfully invited " + invitee.getDisplayName() + " to " + ChatColor.YELLOW + group.getName());
                invitee.sendMessage(ChatColor.GREEN + "You were invited to join the group " + group.getName());
                return true;
            case("join"):
                if(group != null) { player.sendMessage(ChatColor.RED + "You are already in a group!"); return true; }
                Group join = EstraCorePlugin.instance.getGroupManager().getPendingInvite(player);
                if(join == null) { player.sendMessage(ChatColor.RED + "You weren't invited to any groups!"); return true; }
                NameAPI.getGroupManager().getGroup(join.getName()).addMember(player.getUniqueId(), GroupManager.PlayerType.MODS);
                NameAPI.getGroupManager().getGroup(group.getName()).setDefaultGroup(player.getUniqueId());
                join.addMember(player, Rank.MEMBER);
                join.sendGroupMessage("SYSTEM" ,ChatColor.YELLOW + player.getDisplayName() + ChatColor.GREEN + " has joined the group!");
                EstraCorePlugin.instance.getGroupManager().removePendingInvite(player);
                return true;
            case("deny"):
                Group deny = EstraCorePlugin.instance.getGroupManager().getPendingInvite(player);
                if(deny == null) { player.sendMessage(ChatColor.RED + "You weren't invited to any groups!"); return true; }
                EstraCorePlugin.instance.getGroupManager().removePendingInvite(player);
                player.sendMessage(ChatColor.GREEN + "Successfully denied group invite.");
                return true;
            case("chat"):
                if(group == null) { player.sendMessage(ChatColor.RED + "You aren't in a group."); return true; }
                if(args.length > 1) {
                    //Skip the "Chat" argument, and put the rest of the statement there.
                    group.sendGroupMessage(player.getDisplayName() ,Arrays.stream(args).skip(1).collect(Collectors.joining(" ")));
                    return true;
                }
                if(EstraCorePlugin.instance.getGroupManager().getPlayerChatMode(player).getName() == "Group") {
                    EstraCorePlugin.instance.getGroupManager().setPlayerChatMode(player, new GlobalChat());
                    player.sendMessage(ChatColor.GREEN + "You are now in GLOBAL chat.");
                    return true;
                }
                EstraCorePlugin.instance.getGroupManager().setPlayerChatMode(player, new GroupChat());
                player.sendMessage(ChatColor.GREEN + "You are now in GROUP chat.");
                return true;
            case("promote"):
                if(group == null) { player.sendMessage(ChatColor.RED + "You aren't in a group."); return true; }
                if(args.length < 2) { player.sendMessage(ChatColor.RED + "You need to specify a player to promote. /group promote <player>"); return true; }
                Rank rank2 = group.getMemberRank(player);
                if(rank2 != Rank.OWNER) { player.sendMessage(ChatColor.RED + "You do not have permission to promote players!"); return true; }
                Player promote = Bukkit.getPlayer(args[1]);
                if(promote == null) { player.sendMessage(ChatColor.RED + "The player you used either does not exist or is not online!"); return true; }
                if(!group.hasMember(promote)) { player.sendMessage(ChatColor.RED + "That player is not in your group!"); return true; }
                if(group.getMemberRank(promote) == Rank.OWNER || group.getMemberRank(promote) == Rank.OFFICER) { player.sendMessage(ChatColor.RED + "That player cannot be reported!"); return true; }
                NameAPI.getGroupManager().getGroup(group.getName()).removeMember(promote.getUniqueId());
                NameAPI.getGroupManager().getGroup(group.getName()).addMember(promote.getUniqueId(), GroupManager.PlayerType.ADMINS);
                NameAPI.getGroupManager().getGroup(group.getName()).setDefaultGroup(player.getUniqueId());
                group.setMemberRank(promote, Rank.OFFICER);
                promote.sendMessage(ChatColor.GREEN + "You have been promoted to an officer in your group!");
                player.sendMessage(ChatColor.GREEN + "Successfully promoted " + promote.getDisplayName() + ".");
                return true;
            case("demote"):
                if(group == null) { player.sendMessage(ChatColor.RED + "You aren't in a group."); return true; }
                if(args.length < 2) { player.sendMessage(ChatColor.RED + "You need to specify a player to demote. /group demote <player>"); return true; }
                Rank rank3 = group.getMemberRank(player);
                if(rank3 != Rank.OWNER) { player.sendMessage(ChatColor.RED + "You do not have permission to demote players!"); return true; }
                Player demote = Bukkit.getPlayer(args[1]);
                if(demote == null) { player.sendMessage(ChatColor.RED + "The player you used either does not exist or is not online!"); return true; }
                if(!group.hasMember(demote)) { player.sendMessage(ChatColor.RED + "That player is not in your group!"); return true; }
                if(group.getMemberRank(demote) == Rank.MEMBER || group.getMemberRank(demote) == Rank.OWNER) { player.sendMessage(ChatColor.RED + "That player cannot be demoted."); return true; }
                NameAPI.getGroupManager().getGroup(group.getName()).removeMember(demote.getUniqueId());
                NameAPI.getGroupManager().getGroup(group.getName()).addMember(demote.getUniqueId(), GroupManager.PlayerType.MODS);
                NameAPI.getGroupManager().getGroup(group.getName()).setDefaultGroup(player.getUniqueId());
                group.setMemberRank(demote, Rank.MEMBER);
                demote.sendMessage(ChatColor.RED + "You have been demoted in your group.");
                player.sendMessage(ChatColor.GREEN + "Successfully demoted " + demote.getDisplayName() + ".");
                return true;
            case("kick"):
                if(group == null) { player.sendMessage(ChatColor.RED + "You aren't in a group."); return true; }
                if(args.length < 2) { player.sendMessage(ChatColor.RED + "You need to specify a player to demote. /group demote <player>"); return true; }
                Rank rank4 = group.getMemberRank(player);
                if(rank4 != Rank.OWNER && rank4 != Rank.OFFICER) { player.sendMessage(ChatColor.RED + "You do not have permission to demote players!"); return true; }
                Player kick = Bukkit.getPlayer(args[1]);
                if(kick == null) { player.sendMessage(ChatColor.RED + "The player you used either does not exist or is not online!"); return true; }
                if(!group.hasMember(kick)) { player.sendMessage(ChatColor.RED + "That player is not in your group!"); return true; }
                if(rank4 != Rank.OWNER && group.getMemberRank(kick) == Rank.OFFICER) { player.sendMessage(ChatColor.RED + "Only owners can kick officers in a group."); return true; }
                if(group.getMemberRank(kick) == Rank.OWNER) { player.sendMessage(ChatColor.RED + "You cannot kick an owner from their group!"); return true; }
                NameAPI.getGroupManager().getGroup(group.getName()).removeMember(kick.getUniqueId());
                group.removeMember(kick);
                kick.sendMessage(ChatColor.RED + "You have been kicked from your group!");
                group.sendGroupMessage("SYSTEM", ChatColor.YELLOW + kick.getDisplayName() + ChatColor.RED + " has been kicked from the group.");
                player.sendMessage(ChatColor.GREEN + "Successfully kicked " + kick.getDisplayName());
                return true;
            case("leave"):
                if(group == null) { player.sendMessage(ChatColor.RED + "You aren't in a group."); return true; }
                Rank rank5 = group.getMemberRank(player);
                if(rank5 == Rank.OWNER) { player.sendMessage(ChatColor.RED + "You cannot leave a group you own."); return true; }
                NameAPI.getGroupManager().getGroup(group.getName()).removeMember(player.getUniqueId());
                group.removeMember(player);
                player.sendMessage(ChatColor.RED + "You have left the group.");
                group.sendGroupMessage("SYSTEM", ChatColor.YELLOW + player.getDisplayName() + ChatColor.RED + " has left the group.");
                return true;
            case("list"):
                if(group == null) { player.sendMessage(ChatColor.RED + "You aren't in a group."); return true; }
                player.sendMessage(ChatColor.DARK_GRAY + "-=<" + ChatColor.GOLD + group.getName() + "'s" + ChatColor.DARK_GRAY + " Members>=- \n");
                for(OfflinePlayer member : group.getMembers()) {
                    player.sendMessage(ChatColor.GOLD + member.getName() + ChatColor.DARK_GRAY + " - " + ChatColor.AQUA + group.getMemberRank(member).name());
                }
                return true;
            case("info"):
                if(args.length < 2 && group == null) { player.sendMessage(ChatColor.RED + "You are not in a group, specify a group for information."); return true; }
                try {
                    Group infoGroup = EstraCorePlugin.instance.getGroupManager().getGroupByName(args[1]);
                    if (infoGroup == null) {
                        player.sendMessage(ChatColor.RED + "The group you specified does not exist.");
                        return true;
                    }
                    player.sendMessage(ChatColor.DARK_GRAY + "Group Name: " + ChatColor.YELLOW + infoGroup.getName()
                            + "\n" + ChatColor.DARK_GRAY + "Members: " + ChatColor.YELLOW + infoGroup.getMembers().size()
                            + "\n" + ChatColor.DARK_GRAY + "Owner: " + ChatColor.YELLOW + infoGroup.getOwner().getName());
                    return true;
                    //Ex below only occurs if the person doesn't put an arg.
                } catch (IndexOutOfBoundsException ex) {
                    player.sendMessage(ChatColor.DARK_GRAY + "Group Name: " + ChatColor.YELLOW + group.getName()
                            + "\n" + ChatColor.DARK_GRAY + "Members: " + ChatColor.YELLOW + group.getMembers().size()
                            + "\n" + ChatColor.DARK_GRAY + "Owner: " + ChatColor.YELLOW + group.getOwner().getName());
                    return true;
                }
            default:
        }
        return false;
    }
}
