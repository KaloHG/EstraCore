package net.estra.EstraCore;

import net.estra.EstraCore.command.ExitCommand;
import net.estra.EstraCore.command.GroupCommand;
import net.estra.EstraCore.command.MsgCommand;
import net.estra.EstraCore.listener.ChatListener;
import net.estra.EstraCore.listener.ChunkListener;
import net.estra.EstraCore.manager.GroupManager;
import net.estra.EstraCore.manager.ReinforcementManager;
import org.bukkit.Bukkit;
import vg.civcraft.mc.civmodcore.ACivMod;

import java.util.logging.Logger;

public class EstraCorePlugin extends ACivMod {

    public Logger logger;

    public static EstraCorePlugin instance;

    public GroupManager groupManager;

    public EstraCoreConfig configManager;

    public ReinforcementManager reinManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();
        this.logger = getLogger();
        this.configManager = new EstraCoreConfig(this.getConfig());
        this.groupManager = new GroupManager();

        logger.info("EstraCore is starting");

        if(!getServer().getPluginManager().isPluginEnabled("NameLayer")) {
            logger.severe("NameLayer is not present. Shutting down EstraCore.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);

        //No reason for chunk listener as Reins are disabled temporarily.
        //this.getServer().getPluginManager().registerEvents(new ChunkListener(), this);

        this.getServer().getPluginCommand("group").setExecutor(new GroupCommand());
        this.getServer().getPluginCommand("exit").setExecutor(new ExitCommand());
        this.getServer().getPluginCommand("msg").setExecutor(new MsgCommand());

        //Reins temporarily maintained by Citadel
        //load our types.
        //logger.info("Loading reinforcement types.");
        //this.reinManager = new ReinforcementManager(Bukkit.getWorld(getConfigManager().getReinWorld()));
        //reinManager.setActiveTypes(getConfigManager().getLoadedTypes());

        //reinManager.initializeChunks();
    }

    @Override
    public void onDisable() {

    }

    @Override
    protected String getPluginName() {
        return "EstraCore";
    }

    public GroupManager getGroupManager() { return groupManager; }

    public EstraCoreConfig getConfigManager() { return configManager; }

    public ReinforcementManager getReinManager() { return reinManager; }
}
