package net.estra.EstraCore.command;

import net.estra.EstraCore.EstraCorePlugin;
import net.estra.EstraCore.model.Events.PlayerDmEvent;
import net.estra.EstraCore.model.chat.DirectChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MsgCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "This command can only be used in-game!");
            return true;
        }
        Player player = (Player) commandSender;

        if(args.length < 1) {
            player.sendMessage(ChatColor.RED + "You need to specify a player to message.");
            return true;
        }
        if(args.length == 1) {
            Player reciever = Bukkit.getPlayer(args[0]);
            if(reciever == null) {
                player.sendMessage(ChatColor.RED + "That player is either not online or does not exist.");
                return true;
            }
            EstraCorePlugin.instance.getGroupManager().setPlayerChatMode(player, new DirectChat(reciever));
            player.sendMessage(ChatColor.GREEN + "You have been moved to a " + ChatColor.YELLOW + "DIRECT " + ChatColor.GREEN + "chat with " + reciever.getDisplayName());
            return true;
        }
        if(args.length > 1) {
            Player reciever = Bukkit.getPlayer(args[0]);
            if(reciever == null) {
                player.sendMessage(ChatColor.RED + "That player is either not online or does not exist.");
                return true;
            }
            //Get message without argument.
            String msg = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
            player.sendMessage(ChatColor.LIGHT_PURPLE + "To " + reciever.getDisplayName() + "» " + msg);
            reciever.sendMessage(ChatColor.LIGHT_PURPLE + "From " + player.getDisplayName() + "» " + msg);
            if(EstraCorePlugin.instance.getConfigManager().isMessageLoggingEnabled()) {
                EstraCorePlugin.instance.getLogger().info("[DM] " + player.getDisplayName() + " to " + reciever.getName() + ", msg: " + msg);
            }
            Bukkit.getPluginManager().callEvent(new PlayerDmEvent(player, reciever, msg));
            return true;
        }
        return false;
    }
}
