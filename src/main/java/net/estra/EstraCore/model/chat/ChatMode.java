package net.estra.EstraCore.model.chat;

import net.estra.EstraCore.model.ChatModeEnum;

public abstract class ChatMode {

    private String name;

    private ChatModeEnum chatMode;

    public String getName() {
        return this.name;
    }

    public ChatModeEnum getChatMode() {
        return this.chatMode;
    }
}
