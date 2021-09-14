package net.estra.EstraCore.model;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Group {

    private HashMap<OfflinePlayer, Rank> members = new HashMap<>();
    private String name;
    private UUID uuid;

    public Group(String name, OfflinePlayer owner) {
        members.put(owner, Rank.OWNER);
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public HashMap<OfflinePlayer, Rank> getMembersAndRank() { return members; }

    public Set<OfflinePlayer> getMembers() { return members.keySet(); }

    public Rank getMemberRank(OfflinePlayer player) {
        return members.get(player);
    }

    public boolean addMember(OfflinePlayer player, Rank rank) {
        if(members.containsKey(player)) {
            return false;
        }
        members.put(player, rank);
        return true;
    }

    public boolean removeMember(OfflinePlayer player) {
        if(!members.containsKey(player)) {
            return false;
        }
        members.remove(player);
        return true;
    }

    public boolean hasMember(OfflinePlayer player) {
        return members.containsKey(player);
    }

    public OfflinePlayer getOwner() {
        for(OfflinePlayer player : getMembers()) {
            if(members.get(player).equals(Rank.OWNER)) {
                return player;
            }
        }
        return null;
    }

    public boolean setMemberRank(OfflinePlayer player, Rank rank) {
        if(!members.containsKey(player)) {
            return false;
        }
        members.replace(player, rank);
        return true;
    }

    /**
     * Sends a group message to players in your group.
     * @param message
     */
    public void sendGroupMessage(String username, String message) {
        for(OfflinePlayer player : members.keySet()) {
            if(player.isOnline()) {
                String groupMsg = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "GROUP" + ChatColor.DARK_GRAY + "]"
                        + ChatColor.YELLOW + username + ChatColor.DARK_GRAY +  "» " + ChatColor.WHITE + message;
                player.getPlayer().sendMessage(groupMsg);
            }
        }
    }
}
