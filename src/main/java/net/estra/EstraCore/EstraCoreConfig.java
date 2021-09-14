package net.estra.EstraCore;

import net.estra.EstraCore.model.citadel.ReinforcementType;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class EstraCoreConfig {

    private Configuration config;

    //ChatConfig
    private boolean messageLogging;

    private boolean groupLogging;

    private String reinWorld;

    private List<ReinforcementType> loadedTypes;

    public EstraCoreConfig(Configuration config) {
        this.config = config;

        loadChatConfig();
        loadReinConfig();
    }

    private void loadChatConfig() {
        ConfigurationSection chatCfg = config.getConfigurationSection("ChatConfig");
        messageLogging = chatCfg.getBoolean("logMessages");
        groupLogging = chatCfg.getBoolean("logGroupMessages");
    }

    private void loadReinConfig() {
        ConfigurationSection citCfg = config.getConfigurationSection("reinforcements");

        reinWorld = citCfg.getString("world");
        EstraCorePlugin.instance.getLogger().info("ReinWorld: " + reinWorld);

        //parse types.
        ConfigurationSection types = citCfg.getConfigurationSection("types");
        List<ReinforcementType> parsedTypes = new ArrayList<>();
        for(String key : types.getKeys(false)) {
            String name = types.getConfigurationSection(key).getName();
            Material mat = Material.getMaterial(types.getConfigurationSection(key).getString("material"));
            int health = types.getConfigurationSection(key).getInt("health");
            int mature = types.getConfigurationSection(key).getInt("mature");
            if(name == null || mat == null || health == 0) {
                EstraCorePlugin.instance.getLogger().severe("FAILED TO LOAD REINFORCEMENT TYPES FROM CONFIG, SHUTTING DOWN.");
                EstraCorePlugin.instance.getLogger().severe("LOGGING REIN VALUES: " + name + " " + mat + " " + health + " " + mature);
                EstraCorePlugin.instance.getServer().shutdown();
                break;
            }
            ReinforcementType insert = new ReinforcementType(name, mat, health, mature);
            parsedTypes.add(insert);
        }
        loadedTypes = parsedTypes;
    }

    public boolean isMessageLoggingEnabled() { return messageLogging; }

    public boolean isGroupLoggingEnabled() { return groupLogging; }

    public List<ReinforcementType> getLoadedTypes() { return loadedTypes; }

    public String getReinWorld() {
        return reinWorld;
    }
}
