package net.estra.EstraCore;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class EstraCoreConfig {

    private Configuration config;

    //ChatConfig
    private boolean messageLogging;

    private boolean groupLogging;

    public EstraCoreConfig(Configuration config) {
        this.config = config;


        loadChatConfig();
    }

    private void loadChatConfig() {
        ConfigurationSection chatCfg = config.getConfigurationSection("ChatConfig");
        messageLogging = chatCfg.getBoolean("logMessages");
        groupLogging = chatCfg.getBoolean("logGroupMessages");
    }

    public boolean isMessageLoggingEnabled() { return messageLogging; }

    public boolean isGroupLoggingEnabled() { return groupLogging; }
}
