package net.estra.EstraCore.manager;

import net.estra.EstraCore.model.Group;
import net.estra.EstraCore.model.chat.ChatMode;
import net.estra.EstraCore.model.chat.GlobalChat;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupManager {

    private List<Group> groups = new ArrayList<Group>();
    private HashMap<OfflinePlayer, ChatMode> chatModeList = new HashMap<>();
    private HashMap<OfflinePlayer, Group> pendingInvites = new HashMap<>();

    /**
     * Constructor for future use maybe?
     */
    public GroupManager() {}

    public void createGroup(String name,OfflinePlayer player) {
        Group newGroup = new Group(name, player);

        groups.add(newGroup);
    }

    public void removeGroup(Group group) {
        if(groups.contains(group)) { groups.remove(group); }
    }

    public boolean isPlayerInGroup(OfflinePlayer player) {
        for(Group group : groups) {
            if(group.hasMember(player)) { return true; }
        }
        return false;
    }

    public boolean putPendingInvite(OfflinePlayer player, Group group) {
        if(pendingInvites.containsKey(player)) {
            //Player needs to deny pending invite before joining a group.
            return false;
        }
        pendingInvites.put(player, group);
        return true;
    }

    public Group getPendingInvite(OfflinePlayer player) {
        return pendingInvites.get(player);
    }

    public boolean removePendingInvite(OfflinePlayer player) {
        if(pendingInvites.containsKey(player)) {
            pendingInvites.remove(player);
            return true;
        }
        return false;
    }

    public Group getPlayerGroup(OfflinePlayer player) {
        for(Group group : groups) {
            if(group.hasMember(player)) { return group; }
        }
        return null;
    }

    /**
     * Fetches the players chat mode, defaults to global if not set.
     * @param player
     * @return ChatMode instance
     */
    public ChatMode getPlayerChatMode(OfflinePlayer player) {
        if(!chatModeList.containsKey(player)) {
            chatModeList.put(player, new GlobalChat());
            return chatModeList.get(player);
        }
        return chatModeList.get(player);
    }

    public boolean setPlayerChatMode(OfflinePlayer player, ChatMode mode) {
        if(chatModeList.containsKey(player)) {
            chatModeList.replace(player, mode);
            return true;
        }
        chatModeList.put(player, mode);
        return true;
    }
}
