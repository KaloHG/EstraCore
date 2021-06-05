package net.estra.EstraCore;

import net.estra.EstraCore.command.ExitCommand;
import net.estra.EstraCore.command.GroupCommand;
import net.estra.EstraCore.command.MsgCommand;
import net.estra.EstraCore.listener.ChatListener;
import net.estra.EstraCore.manager.GroupManager;
import vg.civcraft.mc.civmodcore.ACivMod;

import java.util.logging.Logger;

public class EstraCorePlugin extends ACivMod {

    public Logger logger;

    public static EstraCorePlugin instance;

    public GroupManager groupManager;

    public EstraCoreConfig configManager;

    @Override
    public void onEnable() {
        instance = this;
        this.logger = getLogger();
        this.groupManager = new GroupManager();
        saveDefaultConfig();
        reloadConfig();

        logger.info("EstraCore is starting up.");
        logger.info("-OEDO-OEDO-OEDO-OEDO-OEDO-");

        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        this.getServer().getPluginCommand("group").setExecutor(new GroupCommand());
        this.getServer().getPluginCommand("exit").setExecutor(new ExitCommand());
        this.getServer().getPluginCommand("msg").setExecutor(new MsgCommand());
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
}
