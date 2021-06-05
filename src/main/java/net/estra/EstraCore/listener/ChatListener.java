package net.estra.EstraCore.listener;

import net.estra.EstraCore.EstraCorePlugin;
import net.estra.EstraCore.model.Group;
import net.estra.EstraCore.model.chat.ChatMode;
import net.estra.EstraCore.model.chat.DirectChat;
import net.estra.EstraCore.model.chat.GlobalChat;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener implements Listener {

    /**
     * I'm unsure what the differences are between PlayerChatEvent and Async, I'll figure it out someday.
     * @param event
     */
    @EventHandler
    public void handleChat(final AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ChatMode chatMode = EstraCorePlugin.instance.getGroupManager().getPlayerChatMode(player);

        switch (chatMode.getName()) {
            case ("Global"):
                //Global chat, just return and permit normal behavior after modifying message slightly.
                //If player isn't in group, then don't modify.
                if(EstraCorePlugin.instance.groupManager.isPlayerInGroup(player)) {
                    Group playerGroup = EstraCorePlugin.instance.getGroupManager().getPlayerGroup(player);
                    //Modify username in chat message.
                    // [GROUP] Player
                    String newName = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + playerGroup.getName() + ChatColor.DARK_GRAY + "] " + player.getDisplayName();
                    event.setFormat(String.format(newName, event.getMessage()));
                }
                return;
            case ("Direct"):
                //cancel the ChatEvent that way the entire server doesn't see the message, and use plugin messages instead.
                event.setCancelled(true);
                //Direct, instantiate
                DirectChat dm = (DirectChat) chatMode;
                OfflinePlayer offPlayer = dm.getReciever();
                if(!offPlayer.isOnline()) {
                    player.sendMessage(ChatColor.RED + "That player has gone offline, and cannot be messaged. You have been automatically set to global chat.");
                    EstraCorePlugin.instance.getGroupManager().setPlayerChatMode(player, new GlobalChat());
                    return;
                }
                Player reciever = dm.getReciever().getPlayer();
                player.sendMessage(ChatColor.LIGHT_PURPLE + "To " + reciever.getDisplayName() + "» " + event.getMessage());
                reciever.sendMessage(ChatColor.LIGHT_PURPLE + "From " + player.getDisplayName() + "» " + event.getMessage());
                if(EstraCorePlugin.instance.getConfigManager().isMessageLoggingEnabled()) {
                    EstraCorePlugin.instance.getLogger().info("[DM] " + player.getDisplayName() + " to " + reciever.getName() + ", msg: " + event.getMessage());
                }
                //both players recieved, we're done here.
                return;
            case ("Group"):
                //cancel event, no reason to broadcast to the entire server :/
                event.setCancelled(true);
                //No reason to instantiate group, we just know its a group message though.
                Group group = EstraCorePlugin.instance.getGroupManager().getPlayerGroup(player);
                if(group == null) { player.sendMessage(ChatColor.RED + "You are not in a group, so you cannot chat in a group!"); return; }
                //Otherwise
                group.sendGroupMessage(player.getDisplayName() ,event.getMessage());
                //Done handling.
                return;
        }
    }
}
