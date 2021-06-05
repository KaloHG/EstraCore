package net.estra.EstraCore.model.chat;

import net.estra.EstraCore.model.ChatModeEnum;

public class GroupChat extends ChatMode{

    @Override
    public String getName() {
        return "Group";
    }

    @Override
    public ChatModeEnum getChatMode() {
        return ChatModeEnum.GROUP;
    }
}
