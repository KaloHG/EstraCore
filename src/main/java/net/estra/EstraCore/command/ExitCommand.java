package net.estra.EstraCore.command;

import net.estra.EstraCore.EstraCorePlugin;
import net.estra.EstraCore.model.chat.GlobalChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        //we dont care about args the fuckwit ran the command and thats ALL I CARE ABOUT.
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "This command can only be used in-game!");
            return true;
        }
        Player player = (Player) commandSender;

        EstraCorePlugin.instance.getGroupManager().setPlayerChatMode(player, new GlobalChat());
        player.sendMessage(ChatColor.GREEN + "You have been moved to " + ChatColor.YELLOW + "GLOBAL" + ChatColor.GREEN  + " chat.");
        return true;
    }
}
