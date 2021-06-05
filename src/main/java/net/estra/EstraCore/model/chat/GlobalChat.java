package net.estra.EstraCore.model.chat;

import net.estra.EstraCore.model.ChatModeEnum;

public class GlobalChat extends ChatMode {

    @Override
    public ChatModeEnum getChatMode() {
        return ChatModeEnum.GLOBAL;
    }

    @Override
    public String getName() {
        return "Global";
    }
}
