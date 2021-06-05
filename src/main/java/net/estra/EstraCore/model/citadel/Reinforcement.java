package net.estra.EstraCore.model.citadel;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Reinforcement {

    private ReinforcementType type;
    private Block block;
    private Location location;

    private int health;
    private long timeRein;

    /**
     * Initial Instantiation of a Rein, when rein'd by a player.
     * @param block
     * @param type
     * @param location
     */
    public Reinforcement(Block block, ReinforcementType type, Location location) {
        this.type = type;
        this.block = block;
        this.health = type.getHealth();
        this.location = location;
        this.timeRein = System.currentTimeMillis();
    }

    public int getHealth() {
        return health;
    }

    public Block getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }

    public ReinforcementType getType() {
        return type;
    }

    public boolean isMature() {
        long timeSince = System.currentTimeMillis() - timeRein;
        long timeTillMat = (long) type.getMatureTime() * 60 * 1000;
        return timeSince > timeTillMat;
    }
}
