package net.estra.EstraCore.model.chat;

import net.estra.EstraCore.model.ChatModeEnum;
import org.bukkit.OfflinePlayer;

public class DirectChat extends ChatMode{

    private OfflinePlayer reciever;

    public DirectChat(OfflinePlayer reciever) {
        this.reciever = reciever;
    }

    @Override
    public String getName() {
        return "Direct";
    }

    @Override
    public ChatModeEnum getChatMode() {
        return ChatModeEnum.DM;
    }

    public OfflinePlayer getReciever() {
        return reciever;
    }
}
