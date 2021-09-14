package net.estra.EstraCore.dao;

import vg.civcraft.mc.civmodcore.ACivMod;
import vg.civcraft.mc.civmodcore.dao.ManagedDatasource;

public class CoreDao extends ManagedDatasource {
    public CoreDao(ACivMod plugin, String user, String pass, String host, int port, String database, int poolSize, long connectionTimeout, long idleTimeout, long maxLifetime) {
        super(plugin, user, pass, host, port, database, poolSize, connectionTimeout, idleTimeout, maxLifetime);
        prepareMigrations();
        updateDatabase();
    }

    /**
     * We have multiple tables that we want to initialize when this is loaded, notably
     * - GROUP table (group members and shit)
     * - REINFORCEMENT table (Reinforcements)
     * - SNITCH table (Snitches)
     */
    private void prepareMigrations() {
        registerMigration(0, false,"CREATE TABLE IF NOT EXISTS groups (`owner` VARCHAR(100) NOT NULL, `groupname` VARCHAR(100) NOT NULL, `members` LONGTEXT NOT NULL);");
    }
}
